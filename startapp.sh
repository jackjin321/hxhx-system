#!/bin/bash
kill -9 $(netstat -nlp | grep :7090 | awk '{print $7}' | awk -F"/" '{ print $1 }')
sleep 3
BASE_PATH=/home/work/java-prod/hxhx-app
# 环境
PROFILES_ACTIVE=prod
# heapError 存放路径
HEAP_ERROR_PATH=$BASE_PATH/heapError
# JVM 参数
JAVA_OPS="-Xms512m -Xmx512m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$HEAP_ERROR_PATH"
dname=`date "+%Y%m%d%H%M%S"`
cp ${BASE_PATH}/meme-app.jar ${BASE_PATH}/meme-app-${dname}.jar
mv ${BASE_PATH}/meme-app-2.6.jar ${BASE_PATH}/meme-app.jar
nohup java $JAVA_OPS -jar ${BASE_PATH}/meme-app.jar --spring.profiles.active=${PROFILES_ACTIVE} 2>&1 >> ${BASE_PATH}/app_meme_$(date +'%Y%m%d').log &
sleep 3
