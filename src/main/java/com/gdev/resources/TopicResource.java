package com.gdev.resources;

import com.gdev.api.TopicListResponse;
import com.gdev.api.TopicOffsetsResponse;
import com.gdev.api.TopicResponse;
import com.gdev.client.Zookeeper;
import com.gdev.core.cache.TopicOffsetsCache;
import com.gdev.core.cache.model.TopicPartitionOffset;
import com.gdev.db.zookeeper.Topic;
import com.gdev.db.zookeeper.ZkDataConverter;
import com.gdev.utils.ZkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.ehcache.Cache;
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
public class TopicResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicResource.class);
    private String zookeeperUrl;
    private ZkUtils zookeeper;
    private Cache topicsOffsets;
    public TopicResource(String zookeeperUrl) {
        this.zookeeperUrl = zookeeperUrl;
        this.zookeeper = Zookeeper.getZkUtils(this.zookeeperUrl);
        this.topicsOffsets = TopicOffsetsCache.getInstance();
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

    @Path("/topics/offsets")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TopicOffsetsResponse getTopicOffsetsResponse( @PathParam("topic") String topicName) {

        List<TopicPartitionOffset> offsets = new ArrayList<TopicPartitionOffset>();
        Iterator it = topicsOffsets.iterator();
        while(it.hasNext()) {
            Cache.Entry<String, Long> lagCache = (Cache.Entry<String, Long>)it.next();
            offsets.add(new TopicPartitionOffset(lagCache.getKey().split("=")[0],lagCache.getKey().split("=")[1], lagCache.getValue() ));
        }
        TopicOffsetsResponse topicResponse = new TopicOffsetsResponse(offsets);

        return topicResponse;
    }
}
