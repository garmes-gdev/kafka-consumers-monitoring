package com.gdev.db.zookeeper;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClusterId {

    @JsonProperty
    int version;
    @JsonProperty
    String id;

    public ClusterId(int version, String id) {
        this.version = version;
        this.id = id;
    }

    public ClusterId() {
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
