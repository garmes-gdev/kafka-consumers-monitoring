package com.gdev.resources;

import com.gdev.api.BrokersResponse;
import com.gdev.api.GroupLagResponse;
import com.gdev.api.GroupListResponse;
import com.gdev.client.Zookeeper;
import com.gdev.core.cache.DataPoint;
import com.gdev.core.cache.LagCache;
import com.gdev.core.cache.LagDataPoint;
import com.gdev.utils.Prometheus;
import com.gdev.utils.ZkUtils;

import javax.cache.Cache;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Path("/")
public class GroupResource {


    private Cache cacheConsumers;
    private Cache lagCache;

    public GroupResource(Cache cacheConsumers) {
        this.cacheConsumers = cacheConsumers;
        this.lagCache = LagCache.getInstance();

    }

    @Path("/groups")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GroupListResponse getGroupsResponse() {
        List<String> groups = new ArrayList<String>();
        Iterator it = cacheConsumers.iterator();
        while(it.hasNext()) {
            Cache.Entry<String, Long> obj = (Cache.Entry<String, Long>)it.next();
            String group = obj.getKey().split("#")[0];
            if(!groups.contains(group)){
                groups.add(group);
            }
        }
        GroupListResponse groupListResponse = new GroupListResponse(groups);
        return groupListResponse;
    }

    @Path("/groups/lag")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GroupLagResponse getGroupResponse() {
        List<LagDataPoint> groups = new ArrayList<LagDataPoint>();
        Iterator it = lagCache.iterator();
        while(it.hasNext()) {
            Cache.Entry<String, LagDataPoint> lagCache = (Cache.Entry<String, LagDataPoint>)it.next();
            groups.add(lagCache.getValue());
        }
        GroupLagResponse groupLagResponse = new GroupLagResponse(groups);
        return groupLagResponse;
    }

    @Path("/groups/lag/{min}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GroupLagResponse getGroupPResponse(@PathParam("min") int min) {
        List<LagDataPoint> groups = new ArrayList<LagDataPoint>();
        Iterator it = lagCache.iterator();
        while(it.hasNext()) {
            Cache.Entry<String, LagDataPoint> lagCache = (Cache.Entry<String, LagDataPoint>)it.next();
            if(lagCache.getValue().getLag() > min)
                groups.add(lagCache.getValue());
        }
        GroupLagResponse groupLagResponse = new GroupLagResponse(groups);
        return groupLagResponse;
    }

    @Path("/prometheus/groups/lag/{min}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getGroupPPrometheusResponse(@PathParam("min") int min) {
        List<LagDataPoint> groups = new ArrayList<LagDataPoint>();
        Iterator it = lagCache.iterator();

        while(it.hasNext()) {
            Cache.Entry<String, LagDataPoint> lagCache = (Cache.Entry<String, LagDataPoint>)it.next();
            if(lagCache.getValue().getLag() > min)
                groups.add(lagCache.getValue());
        }

        return Prometheus.toPrometheusFormat(groups);
    }

    @Path("/prometheus/groups/lag1/{min}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getGroupPPrometheus1Response(@PathParam("min") int min) {
        Iterator it = lagCache.iterator();
        StringBuilder builder = new StringBuilder();
        while(it.hasNext()) {
            Cache.Entry<String, LagDataPoint> lagCache = (Cache.Entry<String, LagDataPoint>)it.next();
            if(lagCache.getValue().getLag() > min)
                builder.append(Prometheus.toPrometheusFormat(lagCache.getValue()));
        }
        return builder.toString();
    }

    
}
