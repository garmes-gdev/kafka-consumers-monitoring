package com.gdev.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GroupListResponse extends Response{

    int number;
    List<String> groups;
    int numberOfConsumers;

    public GroupListResponse() {
    }

    public GroupListResponse(List<String> groups) {
        this.number = groups.size();
        this.groups = groups;
    }

    @JsonProperty
    public int getNumber() {
        return number;
    }

    @JsonProperty
    public List<String> getGroups() {
        return groups;
    }


}
