#!/bin/bash
kill -9 $(netstat -nlp | grep :7870 | awk '{print $7}' | awk -F"/" '{ print $1 }')
sleep 3
BASE_PATH=/www/server/java/hxhx-admin
# 环境
PROFILES_ACTIVE=prod
# heapError 存放路径
HEAP_ERROR_PATH=$BASE_PATH/heapError
# JVM 参数
JAVA_OPS="-Xms512m -Xmx1024m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$HEAP_ERROR_PATH"
dname=`date "+%Y%m%d%H%M%S"`
cp ${BASE_PATH}/meme-admin.jar ${BASE_PATH}/meme-admin-${dname}.jar
mv ${BASE_PATH}/meme-admin-2.6.jar ${BASE_PATH}/meme-admin.jar
nohup java $JAVA_OPS -jar ${BASE_PATH}/meme-admin.jar --spring.profiles.active=${PROFILES_ACTIVE} 2>&1 >> ${BASE_PATH}/admin_meme_$(date +'%Y%m%d').log &
sleep 3