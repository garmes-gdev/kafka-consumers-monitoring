package com.gdev.db.zookeeper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicConf {

    //{"version":1,"config":{}}

    int version;
    Map<String,String> config;

    public TopicConf() {
    }

    public TopicConf(int version, Map<String,String> config) {
        this.version = version;
        this.config = config;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Map<String,String> getConfig() {
        return config;
    }

    public void setConfig(Map<String,String> config) {
        this.config = config;
    }
}
