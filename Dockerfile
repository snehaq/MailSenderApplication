FROM centos

MAINTAINER Glen for eTAAP

# yum update 
RUN yum -y update && yum install -y \
				wget

ENV JDK_VERSION 7u79
ENV JDK_DOWNLOAD_URL "http://download.oracle.com/otn-pub/java/jdk/$JDK_VERSION-b15/jdk-$JDK_VERSION-linux-x64.tar.gz"

RUN cd /tmp && wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" $JDK_DOWNLOAD_URL && tar xzf jdk-$JDK_VERSION-linux-x64.tar.gz && tar xzf jdk-$JDK_VERSION-linux-x64.tar.gz && mv jdk1.7.0_79/ /usr/local/

ENV TOMCAT_VERSION 7.0.73
ENV TOMCAT_DOWNLOAD_URL http://redrockdigimark.com/apachemirror/tomcat/tomcat-7/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz

# Tomcat installation
RUN cd /tmp && wget $TOMCAT_DOWNLOAD_URL && tar xzf apache-tomcat-$TOMCAT_VERSION.tar.gz && mv apache-tomcat-$TOMCAT_VERSION /usr/local/ && export TOMCAT_HOME=/usr/local/apache-tomcat-$TOMCAT_VERSION

ENV JAVA_HOME /usr/local/jdk1.7.0_79
ENV TOMCAT_HOME /usr/local/apache-tomcat-$TOMCAT_VERSION
ENV M2_HOME /usr/local/apache-maven-$MAVEN_VERSION

ENV ANT_VERSION 1.9.8
ENV ANT_DOWNLOAD_URL http://redrockdigimark.com/apachemirror/ant/binaries/apache-ant-$ANT_VERSION-bin.tar.gz

RUN wget $ANT_DOWNLOAD_URL && tar xzf apache-ant-$ANT_VERSION-bin.tar.gz && mv apache-ant-$ANT_VERSION /usr/local/

ENV ANT_HOME /usr/local/apache-$ANT_VERSION
ENV PATH $PATH:$JAVA_HOME/bin/:$TOMCAT_HOME/bin/:/usr/local/apache-ant-$ANT_VERSION/bin/

RUN yum install -y mysql
#RUN mkdir $TOMCAT_HOME/webapps/
RUN mkdir /src
WORKDIR /src
ADD . /src

EXPOSE 8080

LABEL name="eTAAP CentOS" vendor="eTouch" license="GPLv2" build-date="20161130"

CMD ["catalina.sh", "run"]
