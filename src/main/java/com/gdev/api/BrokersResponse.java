package com.gdev.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdev.db.zookeeper.Broker;

import java.util.List;

public class BrokersResponse extends Response{

    List<Broker> data ;

    public List<Broker> getData() {
        return data;
    }

    public void setData(List<Broker> data) {
        this.data = data;
    }
}
