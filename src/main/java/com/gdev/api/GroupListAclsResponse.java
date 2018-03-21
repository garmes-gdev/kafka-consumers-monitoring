package com.gdev.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdev.db.zookeeper.AclResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupListAclsResponse extends Response{

    private Map<String,List<AclResource>> data;

    public GroupListAclsResponse() {
        this.data = new HashMap<>();
    }

    public GroupListAclsResponse(Map<String,List<AclResource>> acls) {
        this.data = acls;
    }


    public Map<String,List<AclResource>> getData() {
        return data;
    }

    public void setData(Map<String,List<AclResource>> data) {
        this.data = data;
    }

    public void setData(String group, List<AclResource> data) {
        this.data.put(group, data);
    }
}
