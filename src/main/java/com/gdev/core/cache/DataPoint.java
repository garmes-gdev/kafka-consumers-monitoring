package com.gdev.core.cache;

import java.io.Serializable;

public class DataPoint implements Serializable {

    Long offset;
    Long timestamp;

    public DataPoint(Long offset, Long timestamp) {
        this.offset = offset;
        this.timestamp = timestamp;
    }

    public Long getOffset() {
        return offset;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
