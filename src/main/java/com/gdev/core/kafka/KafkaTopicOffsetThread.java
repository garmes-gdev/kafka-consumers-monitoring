package com.gdev.core.kafka;

import com.gdev.client.Zookeeper;
import com.gdev.core.cache.*;
import com.gdev.core.cache.model.DataPoint;
import com.gdev.core.cache.model.LagDataPoint;
import com.gdev.utils.ZkUtils;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.ehcache.Cache;
import java.util.*;

public class KafkaTopicOffsetThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaTopicOffsetThread.class);

    public static final String GROUP = "group";
    public static final String TOPIC = "topic";
    public static final String PARTITION = "partition";
    public static final String OFFSET = "offset";
    public static final String METADATA = "metadata";
    public static final String TIMESTAMP = "timestamp";


    private KafkaConsumer<byte[], byte[]> consumer ;
    private Cache cacheTopics;
    private Cache cacheConsumers;

    private Cache lagCache;

    private ZkUtils zkUtil ;

    private int refreshSeconds;


    public KafkaTopicOffsetThread(Properties properties_, String zookeeperUrl_, int refreshSeconds_ ){
        this.consumer = new KafkaConsumer<>(properties_);
        this.refreshSeconds = refreshSeconds_;
        this.zkUtil = Zookeeper.getZkUtils(zookeeperUrl_);
        this.cacheTopics = TopicOffsetsCache.getInstance();
        this.cacheConsumers = ConsumersOffsetsCache.getInstance();
        this.lagCache = LagCache.getInstance();
        LOGGER.info(Thread.currentThread().getName()+" start Kafka  Topic offset thread");

    }

    @Override
    public void run() {
        while (true) {

            try {

                updateCache(consumer, cacheTopics);
                lag();

                // thread to sleep for 1000 milliseconds
                LOGGER.info(String.format("sleep %ds ...",this.refreshSeconds));
                Thread.sleep(this.refreshSeconds*1000);

            } catch (Exception e) {
                LOGGER.error(e.getMessage(),e);
            }
        }
    }

    private Long timestamp;
    private void updateCache(KafkaConsumer<byte[], byte[]> consumer, Cache cache){
        //get topics list
        List<String> topics_name_list = this.zkUtil.getChildren(zkUtil.getBrokerTopicsPath());

        topics_name_list.forEach(topic_name -> {
            if(topic_name.startsWith("__")){ return; }
            // get partitions list
            List<String> partitions_list = this.zkUtil.getChildren(zkUtil.getTopicPartitionPath(topic_name));

            List<TopicPartition> partitions = new ArrayList<>();

            partitions_list.forEach(part ->  {
                TopicPartition actualTopicPartition = new TopicPartition(topic_name, Integer.parseInt(part));
                partitions.add(actualTopicPartition);
            });

            try{
                Map<TopicPartition, Long> offsets = consumer.endOffsets(partitions);
                timestamp = System.currentTimeMillis();
                partitions.forEach(partition -> {
                    cache.put(topic_name+"="+partition.partition(),offsets.get(partition) );
                    //LOGGER.info("TOPIC: "+topic_name+" partition: "+partition.partition()+" offset: "+offsets.get(partition) );
                });
            }catch (Exception e){
                LOGGER.error(e.getMessage(),e);
            }

        });
    }


    private void lag(){

        Iterator it = cacheConsumers.iterator();
        while(it.hasNext()) {
            Cache.Entry<String, DataPoint> consumerCache = (Cache.Entry<String, DataPoint>)it.next();
            String consumerCacheKey = consumerCache.getKey();
            String topicPartition = consumerCacheKey.split("#")[1];

            //LOGGER.info("*** "+ consumerCache.getValue().getOffset());

            Long lag = (Long)cacheTopics.get(topicPartition) - (consumerCache.getValue()).getOffset() ;

            if(lag < 0)
                lag = 0L;
            LagDataPoint lagDataPoint = new LagDataPoint(consumerCacheKey.split("#")[0],
                    topicPartition.split("=")[0],
                    topicPartition.split("=")[1], lag,
                    (timestamp - (consumerCache.getValue()).getTimestamp())/1000) ;

            if(LOGGER.isDebugEnabled()){
                if(lag>100)
                    LOGGER.debug( String.format("Group: %s, Topic: %s, Partition: %s, Lag:%d, diff:%ds", lagDataPoint.getGroup(),
                            lagDataPoint.getTopic(), lagDataPoint.getPartition(),lagDataPoint.getLag(),lagDataPoint.getDiff()) );
            }

            lagCache.put(consumerCache.getKey(), lagDataPoint);
        }
    }

}
