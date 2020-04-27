FROM ubuntu:18.04

MAINTAINER Magomed Gadjiev

# Обвновление списка пакетов
RUN apt-get -y update

#
# Установка postgresql
#
RUN apt-get update

# Back to the root author
USER root

##
 # Сборка проекта
 #

 # Установка JDK

RUN apt-get update
RUN apt-get install -y openjdk-8-jdk-headless

RUN apt-get install -y maven

# Копируем исходный код в Docker-контейнер

ENV WORK /opt/BDServer
ADD src/ $WORK/src/
ADD pom.xml $WORK/

# Собираем и устанавливаем пакет

WORKDIR $WORK
# RUN rm -rf target/

RUN mvn package -Dmaven.test.skip=true

# Объявлем порт сервера
EXPOSE 8080

CMD mvn spring-boot:run