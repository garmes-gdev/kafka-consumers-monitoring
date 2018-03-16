package com.gdev;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class KafkaMonitoringCenterConfiguration extends Configuration {

    @Valid
    private String jaasConf = "";

    @Valid
    private boolean kerberos = false;

    @Valid
    private String zookeeperUrls = "localhost:2181";

    @Valid
    private int refreshSeconds = 5;

    @Valid
    private String commonZkRoot = "";

    @Valid
    private String kafkaBrokersList = "localhost:9092" ;


    @Valid
    private String consumerGroupName = "gdev_kafka_monitor";

    @Valid
    private String sidewinderConfigPath = "";

    @Valid
    private boolean enableHistory = false;

    @Valid
    private boolean enableJMX = false;

    @Valid
    private int jmxPort = 9999;

    public String getZookeeperUrls() {
        return zookeeperUrls;
    }

    public void setZookeeperUrls(String zookeeperUrls) {
        this.zookeeperUrls = zookeeperUrls;
    }

    public int getRefreshSeconds() {
        return refreshSeconds;
    }

    public void setRefreshSeconds(int refreshSeconds) {
        this.refreshSeconds = refreshSeconds;
    }

    /**
     * @return the kerberos
     */
    public boolean isKerberos() {
        return kerberos;
    }

    /**
     * @param kerberos
     *            the kerberos to set
     */
    public void setKerberos(boolean kerberos) {
        this.kerberos = kerberos;
    }

    /**
     * @return the jaasConf
     */
    public String getJaasConf() {
        return jaasConf;
    }

    /**
     * @param jaasConf
     *            the jaasConf to set
     */
    public void setJaasConf(String jaasConf) {
        this.jaasConf = jaasConf;
    }

    /**
     * @return the commonZkRoot
     */
    public String getCommonZkRoot() {
        return commonZkRoot;
    }

    /**
     * @param commonZkRoot
     *            the commonZkRoot to set
     */
    public void setCommonZkRoot(String commonZkRoot) {
        this.commonZkRoot = commonZkRoot;
    }

    /**
     * @return the kafkaBroker
     */
    public String getKafkaBrokersList() {
        return kafkaBrokersList;
    }

    /**
     * @param kafkaBrokersList
     *            the kafkaBroker to set
     */
    public void setKafkaBroker(String kafkaBrokersList) {
        this.kafkaBrokersList = kafkaBrokersList;
    }


    /**
     * @return the consumerGroupName
     */
    public String getConsumerGroupName() {
        return consumerGroupName;
    }

    /**
     * @param consumerGroupName
     *            the consumerGroupName to set
     */
    public void setConsumerGroupName(String consumerGroupName) {
        this.consumerGroupName = consumerGroupName;
    }

    /**
     * @return the enableHistory
     */
    public boolean isEnableHistory() {
        return enableHistory;
    }

    /**
     * @param enableHistory
     *            the enableHistory to set
     */
    public void setEnableHistory(boolean enableHistory) {
        this.enableHistory = enableHistory;
    }

    /**
     * @return the sidewinderConfigPath
     */
    public String getSidewinderConfigPath() {
        return sidewinderConfigPath;
    }

    /**
     * @param sidewinderConfigPath
     *            the sidewinderConfigPath to set
     */
    public void setSidewinderConfigPath(String sidewinderConfigPath) {
        this.sidewinderConfigPath = sidewinderConfigPath;
    }

    /**
     * @return the jmxPort
     */
    public int getJmxPort() {
        return jmxPort;
    }

    /**
     * @param jmxPort the jmxPort to set
     */
    public void setJmxPort(int jmxPort) {
        this.jmxPort = jmxPort;
    }

    /**
     * @return the enableJMX
     */
    public boolean isEnableJMX() {
        return enableJMX;
    }

    /**
     * @param enableJMX the enableJMX to set
     */
    public void setEnableJMX(boolean enableJMX) {
        this.enableJMX = enableJMX;
    }
}
