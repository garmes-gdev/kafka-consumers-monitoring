# Kafka Monitoring Center

How to start the Kafka Monitoring Center application
---

1. Run `mvn clean package` to build your application
2. move the tar/zip
3. Unzip `tar -xvf kafka-monitoring-center-{version}-distribution.tar.gz`
4. start the application `./bin/kafka-monitoring.sh start`
4. To check that your application is running enter url `http://localhost:9090/api/test`

start the the graphical interface
---
1. ``$cd ng-http; python -m SimpleHTTPServer 8000 ``


Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`


JVM Performance Options
---

``
-server -XX:+UseG1GC -XX:MetaspaceSize=96m -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -XX:G1HeapRegionSize=16M -XX:MinMetaspaceFreeRatio=50 -XX:MaxMetaspaceFreeRatio=80 -XX:+ExplicitGCInvokesConcurrent -Djava.awt.headless=true
``


API
---
    #test
    GET     /api/test
    #Cluster Info
    GET     /api/cluster
    GET     /api/cluster/status
    #Brokers Info
    GET     /api/brokers
    #Topics Info
    GET     /api/topics
    GET     /api/topic/{topic}
    GET     /api/topics/offsets
    #Consumer Groups Info
    GET     /api/groups
    GET     /api/groups/acls
    GET     /api/groups/lag
    GET     /api/groups/lag/{min}
    GET     /api/prometheus/groups/lag
    GET     /api/prometheus/groups/lag/{min}    


Support
---
File bug reports, feature requests and questions using [GitHub Issues]

coming soon
----