#!/bin/bash

[ -z $1 ] && exit 1

readonly SOA_CONF="$(cd $(dirname $0);pwd)"

#echo $SOA_CONF && exit 1 

#[ ! -d $SOA_CONF/webappsbak ] &&  mkdir $SOA_CONF/webappsbak

kill `pgrep -lf tomcat |grep /opt/apache-tomcat | awk '{print $1}'` > /dev/null 2>&1

echo "unzip $1 ing ..."
unzip $1 -d $SOA_CONF/pridervip

cd /opt/apache-tomcat/bin && ./startup.sh
