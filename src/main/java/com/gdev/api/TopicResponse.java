package com.gdev.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdev.db.zookeeper.Topic;

public class TopicResponse extends Response{

    @JsonProperty
    Topic data;


    public Topic getData() {
        return data;
    }

    public void setData(Topic data) {
        this.data = data;
    }

    public TopicResponse() {
    }
}
