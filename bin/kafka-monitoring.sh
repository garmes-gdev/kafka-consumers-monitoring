#!/usr/bin/env bash


if [ -z "$JAVA_HOME" ]; then
  JAVA="java"
else
  JAVA="$JAVA_HOME/bin/java"
fi

JMX_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false  -Dcom.sun.management.jmxremote.ssl=false "

JMX_OPTS="$JMX_OPTS -Dcom.sun.management.jmxremote.port=9181 "

JVM_PERFORMANCE_OPTS="-server -XX:+UseG1GC -XX:MetaspaceSize=96m -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -XX:G1HeapRegionSize=16M -XX:MinMetaspaceFreeRatio=50 -XX:MaxMetaspaceFreeRatio=80 -XX:+ExplicitGCInvokesConcurrent -Djava.awt.headless=true "

JVM_PERFORMANCE_OPTS="$JVM_PERFORMANCE_OPTS -XX:MaxDirectMemorySize=1G "

JAVA_HEAP_OPTS="-Xmx1G -Xms1G"


getPID()
{
  PID=$(ps -ef | grep java | grep kafka-monitoring-center | grep -v grep | awk '{print $2}')
  echo $PID
}

start() {
  echo -n "Starting :"
  exec nohup $JAVA  $JAVA_HEAP_OPTS $JVM_PERFORMANCE_OPTS $JMX_OPTS -jar kafka-monitoring-center-*.jar server conf.yml > kafka_monitoring.log 2>&1 < /dev/null &
  echo " done."
}

stop() {
  pid=$(getPID)
  if [ "$pid" = "" ] ; then
    echo "No process found"
  else
    echo -n "Stopping : $pid"
    kill $pid
    # wait for 5 min max (600 x 0.5 sec), then force hardstop
    T=0
    while $0 status >/dev/null && [ $T -le 600 ]; do sleep 0.5; T=$(expr $T + 1); done
    [ $T -ge 600 ] && hardstop || echo " done."
  fi
}

hardstop() {
  pid=$(getPID)
  if [ "$pid" = "" ] ; then
    echo "No process found"
  else
    echo -n "Stopping (hard): $pid"
    kill -9 $pid
    while $0 status > /dev/null; do sleep 0.5; done
    echo " done."
  fi
}

status() {
  pid=$(getPID)
  if [ "$pid" = "" ] ; then
    echo "Stopped"
    exit 3
  else
    echo "Running $pid"
    exit 0
  fi
}

restart() {
  stop
  start
}

case "$1" in
  start)
    start
    ;;

  stop)
    stop
    ;;
  hardstop)
    hardstop
    ;;

  status)
    status
    ;;

  restart)
    stop
    start
    ;;

  *)
    echo "Usage: $0 {start|stop|hardstop|status|restart}"
    exit 1
    ;;

esac