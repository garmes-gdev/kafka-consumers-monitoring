package com.gdev.resources;

import com.gdev.api.ClusterResponse;
import com.gdev.api.ClusterStatusResponse;
import com.gdev.client.Zookeeper;
import com.gdev.db.zookeeper.Cluster;
import com.gdev.utils.ZkUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/")
public class ClusterResource {

    String zookeeperUrl;
    ZkUtils zookeeper;
    public ClusterResource(String zookeeperUrl) {
        this.zookeeperUrl = zookeeperUrl;
        this.zookeeper = Zookeeper.getZkUtils(this.zookeeperUrl);
    }

    @Path("/cluster")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ClusterResponse getClusterResponse() {
        ClusterResponse clusterResponse = new ClusterResponse();
        try {
            clusterResponse.setData(Zookeeper.getClusterId(this.zookeeper));
        } catch (IOException e) {
            clusterResponse.setError(1);
            clusterResponse.setMessage(e.getMessage());
        }
        return clusterResponse;
    }

    @Path("/cluster/status")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ClusterStatusResponse getClusterStatusResponse() {
        ClusterStatusResponse clusterStatusResponse = new ClusterStatusResponse();
        try {

            Cluster cluster = new Cluster();
            cluster.setClusterId(Zookeeper.getClusterId(this.zookeeper));
            cluster.setNumberOfBrokers(Zookeeper.getNubOfBrokers(this.zookeeper));
            cluster.setNumberOfTopics(Zookeeper.getNubOfTopics(this.zookeeper));
            clusterStatusResponse.setData(cluster );

        } catch (IOException e) {
            clusterStatusResponse.setError(1);
            clusterStatusResponse.setMessage(e.getMessage());
        }
        return clusterStatusResponse;
    }

}
