package com.gdev.db.zookeeper;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZkDataConverter {

    private static ObjectMapper mapper = new ObjectMapper();


    public static ClusterId getClusterID(String data) throws IOException {
        ClusterId clusterId = mapper.readValue(data, ClusterId.class);
        return clusterId;
    }

    public static Broker getBroker(String id ,String data) throws IOException {
        Broker broker = mapper.readValue(data, Broker.class);
        broker.setId(id);
        return broker;
    }

    public static List<Broker> getBrokers(Map<String,String> data) throws IOException {
        List<Broker> brokers = new ArrayList<Broker>(data.size());
        int i=0;
        for(Map.Entry<String, String> entry : data.entrySet()) {
            if (entry.getValue().length() != 0){
                brokers.add(i,getBroker(entry.getKey(), entry.getValue()));
                i++;
            }
        }
        return brokers;
    }

    public static TopicMetadata getTopicMetadata(String data) throws IOException {
        TopicMetadata metadata = mapper.readValue(data, TopicMetadata.class);
        return metadata;
    }

    public static TopicConf getTopicConf(String data) throws IOException {
        TopicConf conf = mapper.readValue(data, TopicConf.class);
        return conf;
    }

    public static String getTopicConfString(TopicConf topicConf) throws IOException {
        return mapper.writeValueAsString(topicConf);
    }

    public static TopicAcls getTopicAcls(String data) throws IOException {
        return mapper.readValue(data, TopicAcls.class);
    }


}
