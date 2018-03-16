package com.gdev.db.zookeeper;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cluster {

    @JsonProperty
    ClusterId clusterId;

    @JsonProperty
    int numberOfBrokers;

    @JsonProperty
    int numberOfTopics;

    public Cluster() {
    }

    public Cluster(ClusterId clusterId, int numberOfBrokers_, int numberOfTopics_) {
        this.clusterId = clusterId;
        this.numberOfBrokers = numberOfBrokers_;
        this.numberOfTopics = numberOfTopics_;
    }

    public ClusterId getClusterId() {
        return clusterId;
    }

    public void setClusterId(ClusterId clusterId) {
        this.clusterId = clusterId;
    }


    public int getNumberOfBrokers() {
        return this.numberOfBrokers;
    }

    public void setNumberOfBrokers(int numberOfBrokers) {
        this.numberOfBrokers = numberOfBrokers;
    }


    public int getNumberOfTopics() {
        return this.numberOfTopics;
    }

    public void setNumberOfTopics(int numberOfTopics) {
        this.numberOfTopics = numberOfTopics;
    }
}
