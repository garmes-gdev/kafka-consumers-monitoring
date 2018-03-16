package com.gdev.db.zookeeper;

public class Topic {

    String name;

    TopicConf conf;
    TopicMetadata metadata;
    TopicAcls acls;

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
    }

    public TopicAcls getAcls() {
        return acls;
    }

    public void setAcls(TopicAcls acls) {
        this.acls = acls;
    }
}
