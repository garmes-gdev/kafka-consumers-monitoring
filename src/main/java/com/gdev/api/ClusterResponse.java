package com.gdev.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdev.db.zookeeper.ClusterId;


public class ClusterResponse  extends Response{

    @JsonProperty
    ClusterId data;

    public ClusterId getData() {
        return data;
    }

    public void setData(ClusterId clusterId) {
        this.data = clusterId;
    }

    public ClusterResponse() {
    }
}
