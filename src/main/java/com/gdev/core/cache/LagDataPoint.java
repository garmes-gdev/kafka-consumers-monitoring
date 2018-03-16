package com.gdev.core.cache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LagDataPoint implements Serializable {

    String group;
    String topic;
    String partition;
    Long lag;
    Long diff;

    public LagDataPoint(String group, String topic, String partition, Long lag, Long diff) {
        this.group = group;
        this.topic = topic;
        this.partition = partition;
        this.lag = lag;
        this.diff = diff;
    }

    @JsonProperty
    public String getGroup() {
        return group;
    }
    @JsonProperty
    public String getTopic() {
        return topic;
    }
    @JsonProperty
    public String getPartition() {
        return partition;
    }

   /* @JsonProperty
    public String getGtp() {
        return String.format("%s#%s-%d", this.group, this.topic,this.getPartition());
    }*/

    @JsonProperty
    public Long getLag() {
        return lag;
    }

    @JsonProperty
    public Long getDiff() {
        return diff;
    }

    public static void main(String[] args){
        LagDataPoint lagDataPoint1 = new LagDataPoint("QM5", "FARM1", "0",0L,0L);
        LagDataPoint lagDataPoint2 = new LagDataPoint("QM4", "FARM1", "0",0L,0L);
        LagDataPoint lagDataPoint3 = new LagDataPoint("FZM", "FARM15", "3",0L,0L);

        System.out.println(lagDataPoint1.hashCode());
        System.out.println(lagDataPoint2.hashCode());
        System.out.println(lagDataPoint3.hashCode());
    }
}
