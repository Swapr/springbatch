FROM openjdk:17-jdk-slim
WORKDIR /SpringBatch
COPY target/SpringBatch-0.0.1-SNAPSHOT.war /SpringBatch/SpringBatch-0.0.1-SNAPSHOT.war
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "SpringBatch-0.0.1-SNAPSHOT.war"]