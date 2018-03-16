package com.gdev.utils;

import org.I0Itec.zkclient.ZkClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mgarmes on 19/07/2017.
 */
public class ZkUtils {


    private String ClusterId = "/cluster/id";
    private String AclsPath = "/kafka-acl";
    private String ConsumersPath = "/consumers";
    private String BrokerIdsPath = "/brokers/ids";
    private String BrokerTopicsPath = "/brokers/topics";
    private String TopicConfigPath = "/config/topics";
    private String TopicAclsPath = AclsPath+"/Topic";
    private String TopicConfigChangesPath = "/config/changes";
    private String ControllerPath = "/controller";
    private String ControllerEpochPath = "/controller_epoch";
    private String ReassignPartitionsPath = "/admin/reassign_partitions";
    private String DeleteTopicsPath = "/admin/delete_topics";
    private String PreferredReplicaLeaderElectionPath = "/admin/preferred_replica_election";
    private String AdminPath = "/admin";

    public String getClusterIdPath() { return this.ClusterId; }

    public String getTopicPath(String topic) {
        return this.BrokerTopicsPath + "/" + topic;
    }

    public String getTopicPartitionPath(String topic) {
        return this.BrokerTopicsPath + "/" + topic+ "/partitions";
    }

    public String getTopicPartitionsPath(String topic) {
        return getTopicPath(topic) + "/partitions";
    }

    public String getTopicPartitionPath(String topic , String partitionId ) {
        return  getTopicPartitionsPath(topic) + "/" + partitionId;
    }

    public String getTopicPartitionLeaderAndIsrPath(String topic, String partitionId){
        return getTopicPartitionPath(topic, partitionId) + "/" + "state";
    }

    public String getDeleteTopicPath(String topic){
        return this.DeleteTopicsPath + "/" + topic ;
    }

    public String getConsumersPath() {
        return ConsumersPath;
    }

    public String getBrokerIdsPath() {
        return BrokerIdsPath;
    }

    public String getBrokerPath(String id) {
        return BrokerIdsPath+ "/" +id;
    }

    public String getBrokerTopicsPath() {
        return BrokerTopicsPath;
    }

    public String getTopicConfigPath(String topic) {
        return TopicConfigPath +"/"+topic;
    }

    public String getTopicAclsPath(String topic) {
        return TopicAclsPath +"/"+topic;
    }

    public String getTopicConfigChangesPath() {
        return TopicConfigChangesPath;
    }

    public String getControllerPath() {
        return ControllerPath;
    }

    public String getControllerEpochPath() {
        return ControllerEpochPath;
    }

    public String getReassignPartitionsPath() {
        return ReassignPartitionsPath;
    }

    public String getDeleteTopicsPath() {
        return DeleteTopicsPath;
    }

    public String getPreferredReplicaLeaderElectionPath() {
        return PreferredReplicaLeaderElectionPath;
    }

    public String getAdminPath() {
        return AdminPath;
    }

    public String getAclsPath() {
        return AclsPath;
    }

    private ZkClient zkClient;

    public ZkUtils(String zkConnect){
        ZKStringSerializer zkStringSerializer = new ZKStringSerializer();
        zkClient = new ZkClient(zkConnect, 30000, 3000, zkStringSerializer);

    }

    public void close(){
        if(zkClient != null) zkClient.close();
    }

    public List<String> getChildren(String path){
        return zkClient.getChildren(path);
    }

    public List<String> getSortedBrokerIdList(){
        return getChildren(BrokerIdsPath);
    }

    public Map<String,String> getBrokerList(){

        List<String> ids = getSortedBrokerIdList();
        ids.add(3,"6");

        Map<String,String> brokers = new HashMap<>(ids.size());
        for (int i=0; i<ids.size();i++){
            String brokerPath = BrokerIdsPath+"/"+ids.get(i);
            brokers.put(ids.get(i),getData(brokerPath) );
        }
        return brokers;
    }


    public List<String> getTopicsNameList(){

        return getChildren(BrokerTopicsPath);
    }



    public String getData(String path){
        String data ;
        try {
            data =  zkClient.readData(path);
        }catch (Exception e){
            e.printStackTrace();
            return "{}";
        }
        return data;
    }

    public Boolean exists(String path){
        return this.zkClient.exists(path);
    }







}
