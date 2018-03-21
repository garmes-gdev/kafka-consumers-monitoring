package com.gdev.utils;

import com.gdev.core.cache.model.LagDataPoint;

import java.util.List;

public class Prometheus {


    public static String toPrometheusFormat(List<LagDataPoint> lagDataPoints) {
        StringBuilder builder = new StringBuilder();
        for (LagDataPoint lagDataPoint : lagDataPoints) {
            builder.append(String.format("%s{topic=\"%s\",group=\"%s\",partition=\"%s\"} %d\n", "kafka_lag",
                    lagDataPoint.getTopic(), lagDataPoint.getGroup(),
                    lagDataPoint.getPartition(), lagDataPoint.getLag()));
        }
        //builder.append("\n");
        return builder.toString();
    }


    public static String toPrometheusFormat(LagDataPoint lagDataPoint) {
            return String.format("%s{topic=\"%s\",group=\"%s\",partition=\"%s\"} %d\n", "kafka_lag",
                    lagDataPoint.getTopic(), lagDataPoint.getGroup(),
                    lagDataPoint.getPartition(), lagDataPoint.getLag());
    }
}
