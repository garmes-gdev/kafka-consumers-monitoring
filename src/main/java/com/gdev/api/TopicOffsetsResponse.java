package com.gdev.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdev.core.cache.model.TopicPartitionOffset;

import java.util.List;

public class TopicOffsetsResponse extends Response{

    @JsonProperty
    List<TopicPartitionOffset> data;


    public List<TopicPartitionOffset> getData() {
        return data;
    }

    public void setData(List<TopicPartitionOffset> data) {
        this.data = data;
    }

    public TopicOffsetsResponse(List<TopicPartitionOffset> data) {
        this.data = data;
    }
}
