package com.gdev.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdev.core.cache.model.LagDataPoint;

import java.util.List;

public class GroupLagResponse extends Response{

    int number;
    List<LagDataPoint> groups;
    int numberOfConsumers;

    public GroupLagResponse() {
    }

    public GroupLagResponse(List<LagDataPoint> groups) {
        this.number = groups.size();
        this.groups = groups;
    }

    @JsonProperty
    public int getNumber() {
        return number;
    }

    @JsonProperty
    public List<LagDataPoint> getGroups() {
        return groups;
    }


}
