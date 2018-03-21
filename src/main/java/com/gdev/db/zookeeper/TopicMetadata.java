package com.gdev.db.zookeeper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicMetadata {

    //{"controller_epoch":22,"leader":0,"version":1,"leader_epoch":34,"isr":[0,1,2]}

    //{"version":1,"partitions":{"2":[2,0,1],"1":[1,2,0],"0":[0,1,2]}}


    public TopicMetadata() {
    }

    public TopicMetadata(Map<String, List<Integer>> partitions, int version) {
        this.partitions = partitions;
        this.version = version;
    }

    private Map<String,List<Integer>> partitions;
    private int version;

    public Map<String, List<Integer>> getPartitions() {
        return partitions;
    }

    public void setPartitions(Map<String, List<Integer>> partitions) {
        this.partitions = partitions;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
