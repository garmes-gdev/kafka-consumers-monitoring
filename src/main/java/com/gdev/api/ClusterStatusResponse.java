package com.gdev.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdev.db.zookeeper.Cluster;
import com.gdev.db.zookeeper.ClusterId;


public class ClusterStatusResponse extends Response{

    @JsonProperty
    Cluster data;

    public Cluster getData() {
        return data;
    }

    public void setData(Cluster cluster) {
        this.data = cluster;
    }

    public ClusterStatusResponse() {
    }


}
