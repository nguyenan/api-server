FROM tomcat:8.0.23-jre8
MAINTAINER "Russell Palmiter" <russell@webutilitykit.com> 

## GENERAL STUFF TO MAKE APT PACKAGES WORK BETTER ##
RUN apt-get update --fix-missing

## INSTALL NTP TO SYNCHRONIZE SERVER TIME ##
#RUN apt-get --yes install ntp

## INSTALL GIT ##
RUN apt-get --yes install git-core

## INSTALL KILLALL ##
RUN apt-get --yes install psmisc

## install wget ##
RUN apt-get install wget

## phantomjs fonts ##
RUN apt-get --yes install fontconfig

ENV CATALINA_HOME /usr/local/tomcat

ENV CATALINA_WEBAPPS ${CATALINA_HOME}/webapps

#################### APPLICATION #################

ADD webapps/email.war ${CATALINA_WEBAPPS}/ 
ADD webapps/storage.war ${CATALINA_WEBAPPS}/
ADD webapps/file.war ${CATALINA_WEBAPPS}/
ADD webapps/image.war ${CATALINA_WEBAPPS}/
ADD webapps/misc.war ${CATALINA_WEBAPPS}/
ADD webapps/help.war ${CATALINA_WEBAPPS}/
#ADD webapps/search.war ${CATALINA_WEBAPPS}/
ADD webapps/payment.war ${CATALINA_WEBAPPS}/
ADD webapps/code.war ${CATALINA_WEBAPPS}/
ADD webapps/services.war ${CATALINA_WEBAPPS}/
ADD webapps/dns.war ${CATALINA_WEBAPPS}/
ADD webapps/cdn.war ${CATALINA_WEBAPPS}/
ADD webapps/user.war ${CATALINA_WEBAPPS}

# PHANTOMJS
RUN mkdir /usr/local/phantomjs 
ADD phantomjs/render.js /usr/local/phantomjs/render.js
ADD phantomjs/phantomjs /usr/local/phantomjs/phantomjs

# SSL
RUN rm -rf ${CATALINA_HOME}/conf/server.xml
ADD server.xml ${CATALINA_HOME}/conf/server.xml
ADD tend.ag.keystore ${CATALINA_HOME}/tend.ag.keystore


# APP DYNAMIC
ADD AppServerAgent-4.2.14.0.zip ./
RUN mkdir -p /opt/appdynamics/appagent
RUN unzip AppServerAgent-4.2.14.0.zip -d /opt/appdynamics/appagent
ENV CATALINA_OPTS="-javaagent:/opt/appdynamics/appagent/javaagent.jar"

EXPOSE 8080
EXPOSE 8443

CMD ["catalina.sh", "run"]
