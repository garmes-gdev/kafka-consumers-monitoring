package com.gdev.core.cache.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TopicPartitionOffset {

    String topic;
    String partition;
    Long offset;

    @JsonProperty
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @JsonProperty
    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    @JsonProperty
    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public TopicPartitionOffset(String topic, String partition, Long offset) {
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
    }
}
