#!/bin/bash
kill -9 $(netstat -nlp | grep :7070 | awk '{print $7}' | awk -F"/" '{ print $1 }')
sleep 3
WORKDIR=/home/work/java-prod/admin
dname=`date "+%Y%m%d%H%M%S"`
cp ${WORKDIR}/meme-admin.jar ${WORKDIR}/meme-admin-${dname}.jar
mv ${WORKDIR}/meme-admin-2.6.jar ${WORKDIR}/meme-admin.jar
nohup java -jar ${WORKDIR}/meme-admin.jar --spring.profiles.active=prod 2>&1 >> ${WORKDIR}/admin_meme_$(date +'%Y%m%d').log &
sleep 3
