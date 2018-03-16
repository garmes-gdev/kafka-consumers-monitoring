# Kafka Monitoring Center

How to start the Kafka Monitoring Center application
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/kafka-monitofing-tool-*.jar server bin/conf.yml`
3. To check that your application is running enter url `http://localhost:9090/api/test`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`


JVM Performance Options
---

``
-server -XX:+UseG1GC -XX:MetaspaceSize=96m -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -XX:G1HeapRegionSize=16M -XX:MinMetaspaceFreeRatio=50 -XX:MaxMetaspaceFreeRatio=80 -XX:+ExplicitGCInvokesConcurrent -Djava.awt.headless=true"
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
    #Consumer Groups Info
    GET     /api/groups
    GET     /api/groups/lag
    GET     /api/groups/lag/{min}
    GET     /api/prometheus/groups/lag/{min}    


Support
---
File bug reports, feature requests and questions using [GitHub Issues]

coming soon
----