package com.gdev.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestResponse extends Response{

    String name;
    String status;

    public TestResponse() {
        this.name = "kafka monitoring tool";
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        this.status = processName.split("@")[0];
    }

    @JsonProperty
    public String getStatus() {
        return status;
    }

    @JsonProperty
    public String getName() {
        return name;
    }
}
