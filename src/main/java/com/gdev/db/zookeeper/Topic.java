package com.gdev.db.zookeeper;

public class Topic {

    String name;

    TopicConf conf;
    TopicMetadata metadata;
    KAcls acls;

    int numberOfPartitions;
    int replicationFactor;

    public Topic() {
    }


    public TopicConf getConf() {
        return conf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setConf(TopicConf conf) {
        this.conf = conf;
    }

    public TopicMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(TopicMetadata metadata) {

        this.metadata = metadata;
        this.numberOfPartitions = this.metadata.getPartitions().size();
        this.replicationFactor = this.metadata.getPartitions().get(this.metadata.getPartitions().keySet().toArray()[0]).size();
    }

    public KAcls getAcls() {
        return acls;
    }

    public void setAcls(KAcls acls) {
        this.acls = acls;
    }

    public int getNumberOfPartitions() {
        return numberOfPartitions;
    }

    public int getReplicationFactor() {
        return replicationFactor;
    }
}
