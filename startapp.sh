#!/bin/bash
kill -9 $(netstat -nlp | grep :7090 | awk '{print $7}' | awk -F"/" '{ print $1 }')
sleep 3
WORKDIR=/home/work/java-prod/app
dname=`date "+%Y%m%d%H%M%S"`
cp ${WORKDIR}/meme-app.jar ${WORKDIR}/meme-app-${dname}.jar
mv ${WORKDIR}/meme-app-2.6.jar ${WORKDIR}/meme-app.jar
nohup java -jar ${WORKDIR}/meme-app.jar --spring.profiles.active=prod 2>&1 >> ${WORKDIR}/app_meme_$(date +'%Y%m%d').log &
sleep 3
