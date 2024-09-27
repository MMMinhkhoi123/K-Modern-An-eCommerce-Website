ARG  jdkVersion=22
FROM openjdk:${jdkVersion}-ea-19

WORKDIR /app
ADD target/app.jar app.jar
ENV DB_URL=jdbc:mysql://mysql-container-2:3306/levent
ENV URL = http://localhost
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

#
#docker run -dp 8080:8080 ^
#   --name app_container ^
#   --network levent_test ^
#   app


#docker run -d ^
#    --name mysql-container-2 ^
#    -p 127.0.0.1:3306:3306 ^
#    --network levent_test --network-alias mysql ^
#    -v levent-mysql-data:/var/lib/mysql ^
#    -e MYSQL_ROOT_PASSWORD=123 ^
#    -e MYSQL_DATABASE=levent ^
#    mysql:8.0
#
#
#    docker network create levent_test


#docker run -d ^
#    --name mysql-container-3 ^
#    -p 127.0.0.1:3306:3306 ^
#    --network levent_test --network-alias mysql ^
#    -v levent-mysql-data:/var/lib/mysql ^
#    -e MYSQL_ROOT_PASSWORD=123 ^
#    -e MYSQL_DATABASE=levent ^
#    mysql:8.0