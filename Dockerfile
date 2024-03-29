#User JDK 17
FROM openjdk:17-jdk-slim

#Author Maintain
MAINTAINER quannguyen

#Copy File Jar to Docker
COPY target/shopee-be-gateway-demo-latest.jar shopee-be-gateway-demo-latest.jar

#Excute command to run Spring boot
#Cmd Example: java -jar target/shopee-be-eureka-demo-0.0.1-SNAPSHOT.jar
ENTRYPOINT [ \
    "java", \
    "-jar", \
    "shopee-be-gateway-demo-latest.jar" \
]

