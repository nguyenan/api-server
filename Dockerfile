FROM rpalmite/webutilitykit
MAINTAINER Russell Palmiter

RUN apt-get install --fix-missing

# INSTALL EVERYTHING FOR A NODEJS SAMPLE APP
#RUN apt-get install -y software-properties-common python
#RUN add-apt-repository ppa:chris-lea/node.js
#RUN echo "deb http://us.archive.ubuntu.com/ubuntu/ precise universe" >> /etc/apt/sources.list
#RUN apt-get update
#RUN apt-get install -y nodejs
#RUN apt-get install -y nodejs=0.6.12~dfsg1-1ubuntu1
RUN mkdir /var/www

# ADD .JS APP
# CMD ["/usr/bin/node", "/var/www/app.js"] 


EXPOSE 8080
EXPOSE 8443
CMD ["catalina.sh", "run"]

