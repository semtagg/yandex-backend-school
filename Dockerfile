FROM openjdk:18-ea-jdk
ADD target/yandex-school-autumn-2022-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar", "-Dserver.port=80", "app.jar"]