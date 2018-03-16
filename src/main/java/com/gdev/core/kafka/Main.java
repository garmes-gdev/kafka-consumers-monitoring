package com.gdev.core.kafka;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;


public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args){

        String brokersList = args[0];
        String zookeeperUrl = args[1];

        Properties props = new Properties();
        props.put("bootstrap.servers", brokersList);
        props.put("group.id", "kafka_monitor");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("enable.auto.commit", "false");
        props.put("auto.offset.reset", "latest");

        //props.put("security.protocol", System.getProperty("security.protocol"));

        for (int i=0;i<2;i++){
            Thread xx = new Thread(new KafkaNewConsumerOffsetThread(props));
            xx.start();
        }

        Thread th = new Thread(new KafkaTopicOffsetThread(props,zookeeperUrl, 30));
        th.start();

      /*  try {
            LOGGER.info("wait Topic offset thread 5s ...");
            Thread.sleep(5000);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }

        Thread lag = new Thread(new LagThread());
        lag.start();*/

    }
}
