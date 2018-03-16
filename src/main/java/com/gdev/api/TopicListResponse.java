package com.gdev.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TopicListResponse extends Response{

    int number;
    List<String> topics;

    public TopicListResponse() {
    }

    public TopicListResponse( List<String> topics) {
        this.number = topics.size();
        this.topics = topics;
    }

    @JsonProperty
    public int getNumber() {
        return number;
    }

    @JsonProperty
    public List<String> getTopics() {
        return topics;
    }
}
