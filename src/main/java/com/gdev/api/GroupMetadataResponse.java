package com.gdev.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdev.core.cache.model.GroupMetadata;

import java.util.List;

public class GroupMetadataResponse extends Response{

    GroupMetadata groupMetadata;

    public GroupMetadataResponse() {
    }

    public GroupMetadataResponse(GroupMetadata groupMetadata) {
        this.groupMetadata = groupMetadata;
    }

    @JsonProperty
    public GroupMetadata getGroupMetadata() {
        return groupMetadata;
    }

    public void setGroupMetadata(GroupMetadata groupMetadata) {
        this.groupMetadata = groupMetadata;
    }

}
