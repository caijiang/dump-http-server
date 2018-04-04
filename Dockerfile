#编译系统
FROM maven:3.5.2-jdk-8 as builder

ADD . /build/
WORKDIR /build/
RUN ["mvn","-DskipTests=true","package"]

#运行系统

FROM registry.cn-shanghai.aliyuncs.com/mingshz/openjdk-utf8:8-jre
WORKDIR /opt/dhs
COPY --from=builder /build/target/dump-http-server-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/dhs/server.jar
COPY src/main/docker/entrance.sh /sbin/entrance.sh
RUN chmod +x /sbin/entrance.sh
EXPOSE 80
ENTRYPOINT ["/sbin/entrance.sh"]
