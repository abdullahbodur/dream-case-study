FROM maven:3.8.8-eclipse-temurin-17 AS Builder
WORKDIR /project
COPY ../pom.xml .
COPY ../app ./app
COPY ../app-performance-test ./app-performance-test
RUN mvn clean package
RUN rm -rf ./app-performance-test


FROM eclipse-temurin:17-jre
WORKDIR /project/app
COPY --from=Builder /project/app/target/*.jar /project/app/application.jar
CMD ["java", "-jar", "application.jar"]
