#!/bin/sh
kill `pgrep -lf huobi-push-dubbo-0.0.1-SNAPSHOT.jar | awk '{print $1}'`  > /dev/null 2>&1

nohup java -jar -Xms2g -Xmx3g -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:PermSize=128M -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:./log/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./log/heapDump.log huobi-push-dubbo-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 &
