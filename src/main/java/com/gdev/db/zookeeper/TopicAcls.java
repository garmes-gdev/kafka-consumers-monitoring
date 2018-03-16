package com.gdev.db.zookeeper;

import java.util.List;
import java.util.Map;

public class TopicAcls {

    //{"version":1,"acls":[AclResource]}


    int version;

    List<AclResource> acls;

    public TopicAcls() {
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<AclResource> getAcls() {
        return acls;
    }

    public void setAcls(List<AclResource> acls) {
        this.acls = acls;
    }
}
