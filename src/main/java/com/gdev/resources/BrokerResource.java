package com.gdev.resources;

import com.gdev.api.BrokersResponse;
import com.gdev.api.ClusterResponse;
import com.gdev.api.ClusterStatusResponse;
import com.gdev.client.Zookeeper;
import com.gdev.db.zookeeper.Cluster;
import com.gdev.utils.ZkUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/")
public class BrokerResource {

    String zookeeperUrl;
    ZkUtils zookeeper;
    public BrokerResource(String zookeeperUrl) {
        this.zookeeperUrl = zookeeperUrl;
        this.zookeeper = Zookeeper.getZkUtils(this.zookeeperUrl);
    }

    @Path("/brokers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public BrokersResponse getClusterResponse() {
        BrokersResponse brokersResponse = new BrokersResponse();
        try {
            brokersResponse.setData(Zookeeper.getBrokers(this.zookeeper));
        } catch (IOException e) {
            brokersResponse.setError(1);
            brokersResponse.setMessage(e.getMessage());
        }
        return brokersResponse;
    }


    
}
