package com.gdev.resources;

import com.gdev.api.TopicListResponse;
import com.gdev.api.TopicResponse;
import com.gdev.client.Zookeeper;
import com.codahale.metrics.annotation.Timed;
import com.gdev.db.zookeeper.Topic;
import com.gdev.db.zookeeper.ZkDataConverter;
import com.gdev.utils.ZkUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/")
public class TopicResource {


    String zookeeperUrl;
    ZkUtils zookeeper;
    public TopicResource(String zookeeperUrl) {
        this.zookeeperUrl = zookeeperUrl;
        this.zookeeper = Zookeeper.getZkUtils(this.zookeeperUrl);
    }


    @Path("/topics")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TopicListResponse getTopicListResponse() {
        return new TopicListResponse(Zookeeper.getZkUtils(zookeeperUrl).getTopicsNameList());
    }


    @Path("/topic/{topic}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TopicResponse getTopicResponse( @PathParam("topic") String topicName) {

        TopicResponse topicResponse = new TopicResponse();
        Topic topicRes = new Topic();
        topicRes.setName(topicName);

        try {
            String metadata = zookeeper.getData(zookeeper.getTopicPath(topicName));
            String conf = zookeeper.getData(zookeeper.getTopicConfigPath(topicName));

            topicRes.setConf(ZkDataConverter.getTopicConf(conf));
            topicRes.setMetadata(ZkDataConverter.getTopicMetadata(metadata) );

            if(zookeeper.exists(zookeeper.getTopicAclsPath(topicName))){
                String acls = zookeeper.getData(zookeeper.getTopicAclsPath(topicName));
                topicRes.setAcls(ZkDataConverter.getTopicAcls(acls));
            }

            topicResponse.setData(topicRes);
        } catch (IOException e) {
            topicResponse.setMessage(e.getMessage());
            topicResponse.setError(1);
        }






        return topicResponse;
    }
}
