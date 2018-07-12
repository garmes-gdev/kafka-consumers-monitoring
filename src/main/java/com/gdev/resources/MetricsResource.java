package com.gdev.resources;

import com.gdev.core.cache.MonitoringMetricsCache;
import com.ldev.kafka.metrics.Gauge;
import com.ldev.kafka.metrics.Histogram;
import com.ldev.kafka.metrics.Meter;
import com.ldev.kafka.metrics.MetricsResult;
import com.ldev.kafka.metrics.Timer;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/")
public class MetricsResource {
    private static final Logger log = LoggerFactory.getLogger(MetricsResource.class);

    private Cache<String, MetricsResult> metricsCache;

    public MetricsResource() {
        this.metricsCache = MonitoringMetricsCache.getInstance();
    }

    @Path("/metrics/{clusterId}/{brokerId}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPrometheusKafkaMetrics(@PathParam("clusterId") String clusterId, @PathParam("brokerId") String brokerId) {
        log.debug("GET prometheus/metrics/" + clusterId + "/" + brokerId);

        StringBuilder sb = new StringBuilder();
        sb.append(getPrometheusKafkaTimerMetrics(clusterId, brokerId));
        sb.append(getPrometheusKafkaMeterMetrics(clusterId, brokerId));
        sb.append(getPrometheusKafkaHistogramMetrics(clusterId, brokerId));
        sb.append(getPrometheusKafkaGaugeMetrics(clusterId, brokerId));

        return sb.toString();
    }

    @Path("/metrics/{clusterId}/{brokerId}/timers")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPrometheusKafkaTimerMetrics(@PathParam("clusterId") String clusterId, @PathParam("brokerId") String brokerId) {
        StringBuilder sb = new StringBuilder();
        MetricsResult r = metricsCache.get(clusterId + "-" + brokerId);
        for (Timer t : r.getTimers()) {
            for (String s : t.getPrometheusStrings('_')) sb.append(s).append("\n");
        }
        return sb.toString();
    }

    @Path("/metrics/{clusterId}/{brokerId}/meters")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPrometheusKafkaMeterMetrics(@PathParam("clusterId") String clusterId, @PathParam("brokerId") String brokerId) {
        StringBuilder sb = new StringBuilder();
        MetricsResult r = metricsCache.get(clusterId + "-" + brokerId);
        for (Meter m : r.getMeters()) {
            for (String s : m.getPrometheusStrings('_')) sb.append(s).append("\n");
        }
        return sb.toString();
    }

    @Path("/metrics/{clusterId}/{brokerId}/histograms")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPrometheusKafkaHistogramMetrics(@PathParam("clusterId") String clusterId, @PathParam("brokerId") String brokerId) {
        StringBuilder sb = new StringBuilder();
        MetricsResult r = metricsCache.get(clusterId + "-" + brokerId);
        for (Histogram h : r.getHistograms()) {
            for (String s : h.getPrometheusStrings('_')) sb.append(s).append("\n");
        }
        return sb.toString();
    }

    @Path("/metrics/{clusterId}/{brokerId}/gauges")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPrometheusKafkaGaugeMetrics(@PathParam("clusterId") String clusterId, @PathParam("brokerId") String brokerId) {
        StringBuilder sb = new StringBuilder();
        MetricsResult r = metricsCache.get(clusterId + "-" + brokerId);
        for (Gauge g : r.getGauges()) {
            for (String s : g.getPrometheusStrings('_')) sb.append(s).append("\n");
        }
        return sb.toString();
    }
}
