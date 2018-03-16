package com.gdev;

import com.gdev.client.Zookeeper;
import com.gdev.core.cache.ConsumersOffsetsCache;
import com.gdev.core.cache.TopicOffsetsCache;
import com.gdev.core.kafka.KafkaNewConsumerOffsetThread;
import com.gdev.core.kafka.KafkaTopicOffsetThread;
import com.gdev.health.TestHealthCheck;
import com.gdev.resources.*;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.cache.Cache;
import java.util.Properties;

public class KafkaMonitoringCenter extends Application<KafkaMonitoringCenterConfiguration> {

    private Cache cacheTopics;
    private Cache cacheConsumers;


    public static void main(String[] args) throws Exception {
        new KafkaMonitoringCenter().run(args);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
               Zookeeper.close();
             }
        });
    }

    @Override
    public String getName() {
        return "Kafka Monitoring Tool";
    }

    @Override
    public void initialize(Bootstrap<KafkaMonitoringCenterConfiguration> bootstrap) {

        cacheTopics = TopicOffsetsCache.getInstance();
        cacheConsumers = ConsumersOffsetsCache.getInstance();
    }

    @Override
    public void run(KafkaMonitoringCenterConfiguration configuration, Environment environment) throws Exception {

        Properties ConsumerProps = new Properties();
        ConsumerProps.put("bootstrap.servers", configuration.getKafkaBrokersList());
        ConsumerProps.put("group.id", "kafka_monitor");
        ConsumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        ConsumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        ConsumerProps.put("enable.auto.commit", "false");
        ConsumerProps.put("auto.offset.reset", "latest");

        environment.jersey().setUrlPattern("/api/*");

        final TestResource testResource = new TestResource();
        final TestHealthCheck healthCheck = new TestHealthCheck();
        environment.healthChecks().register("test", healthCheck);
        environment.jersey().register(testResource);

        final TopicResource topicResource = new TopicResource(configuration.getZookeeperUrls());
        environment.jersey().register(topicResource);

        final ClusterResource clusterResource = new ClusterResource(configuration.getZookeeperUrls());
        environment.jersey().register(clusterResource);

        final BrokerResource brokerResource = new BrokerResource(configuration.getZookeeperUrls());
        environment.jersey().register(brokerResource);

        final GroupResource groupResource = new GroupResource(cacheConsumers);
        environment.jersey().register(groupResource);

        //props.put("security.protocol", System.getProperty("security.protocol"));

        for (int i=0;i<2;i++){
            Thread xx = new Thread(new KafkaNewConsumerOffsetThread(ConsumerProps));
            xx.setName("consumer-"+i);
            xx.start();
        }

        Thread th = new Thread(new KafkaTopicOffsetThread(ConsumerProps,configuration.getZookeeperUrls(), configuration.getRefreshSeconds() ));
        th.start();

    }
}
