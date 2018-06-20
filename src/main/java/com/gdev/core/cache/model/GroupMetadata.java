package com.gdev.core.cache.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupMetadata implements Serializable {

    String protocolType;
    Integer generationId;
    String leaderId;
    String protocol;
    List<GroupMemberMetadata> memberMetadataArray;

    public GroupMetadata(String protocolType, Integer generationId, String leaderId, String protocol) {
        this.protocolType = protocolType;
        this.generationId = generationId;
        this.leaderId = leaderId;
        this.protocol = protocol;
        this.memberMetadataArray = new ArrayList<>();
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public Integer getGenerationId() {
        return generationId;
    }

    public void setGenerationId(Integer generationId) {
        this.generationId = generationId;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public List<GroupMemberMetadata> getMemberMetadataArray() {
        return memberMetadataArray;
    }

    public void setMemberMetadataArray(List<GroupMemberMetadata> memberMetadataArray) {
        this.memberMetadataArray = memberMetadataArray;
    }

    public void addMemberMetadata(GroupMemberMetadata memberMetadata) {
        this.memberMetadataArray.add(memberMetadata);
    }
}
