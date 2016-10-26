#!/bin/bash

DOMAIN=$1

ssh root@${DOMAIN} "service tomcat7 restart"
ssh root@${DOMAIN} "chown tomcat7:tomcat7 /var/lib/tomcat7"
ssh root@${DOMAIN} "chmod a+x /var/lib/tomcat7/webapps/code/linux/ubuntu64/phantomjs/phantomjs"
sleep 10 
curl http://${DOMAIN}/derby/derbynet
