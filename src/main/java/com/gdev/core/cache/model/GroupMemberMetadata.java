package com.gdev.core.cache.model;

import java.io.Serializable;

public class GroupMemberMetadata implements Serializable {

    String memberId;
    String clientId;
    String clientHost;
    Integer sessionTimeout;
    Integer rebalanceTimeout;

    public GroupMemberMetadata(String memberId, String clientId, String clientHost, Integer sessionTimeout, Integer rebalanceTimeout) {
        this.memberId = memberId;
        this.clientId = clientId;
        this.clientHost = clientHost;
        this.sessionTimeout = sessionTimeout;
        this.rebalanceTimeout = rebalanceTimeout;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientHost() {
        return clientHost;
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public Integer getRebalanceTimeout() {
        return rebalanceTimeout;
    }
}
