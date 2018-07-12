package com.gdev.core.kafka;

import com.gdev.core.cache.MonitoringMetricsCache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ldev.kafka.metrics.MetricsResult;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Properties;

public class MonitoringMetricsThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(MonitoringMetricsThread.class);
    private final Cache<String, MetricsResult> metricsCache;

    private KafkaConsumer<String, String> consumer;

    public MonitoringMetricsThread(Properties props, String metricsTopic) {
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(metricsTopic));
        log.info(Thread.currentThread().getName() + " start Kafka metrics consumer");
        metricsCache = MonitoringMetricsCache.getInstance();
    }

    @Override
    public void run() {
        Gson g = new Gson();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                String v = record.value();
                MetricsResult val = g.fromJson(v, new TypeToken<MetricsResult>() {
                }.getType());
                metricsCache.put(val.getClusterBroker(), val);
            }
        }
    }
}
