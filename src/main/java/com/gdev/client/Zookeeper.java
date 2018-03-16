package com.gdev.client;

import com.gdev.db.zookeeper.Broker;
import com.gdev.db.zookeeper.ClusterId;
import com.gdev.db.zookeeper.ZkDataConverter;
import com.gdev.utils.ZkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zookeeper {

    private static ZkUtils zkUtils = null;

    public static ZkUtils getZkUtils(String zookeeperUrl) {
        if(zkUtils == null){
            zkUtils =new ZkUtils(zookeeperUrl);
        }
        return zkUtils;
    }

    public static void close(){
        if(zkUtils != null){
            zkUtils.close();
        }
    }


    public static ClusterId getClusterId(ZkUtils zkUtils_) throws IOException {
       return ZkDataConverter.getClusterID(zkUtils_.getData(zkUtils_.getClusterIdPath()));
    }

    public static int getNubOfBrokers(ZkUtils zkUtils_) throws IOException {
        return zkUtils_.getSortedBrokerIdList().size();
    }

    public static int getNubOfTopics(ZkUtils zkUtils_) throws IOException {
        return zkUtils_.getTopicsNameList().size();
    }

    public static List<Broker> getBrokers(ZkUtils zkUtils_) throws IOException {
        List<String> brokers = zkUtils_.getChildren(zkUtils_.getBrokerIdsPath());
        List<Broker> brokersList = new ArrayList<>(brokers.size());
        for(int i=0; i < brokers.size(); i++){
            Broker b = ZkDataConverter.getBroker(brokers.get(i), zkUtils_.getData(zkUtils_.getBrokerPath(brokers.get(i))) );
            brokersList.add(b);
        }
        return brokersList;
    }
}
