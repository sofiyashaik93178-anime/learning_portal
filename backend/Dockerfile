
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN java -version
RUN mvn -version
RUN mvn -DskipTests package

FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/target/learn-hub-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
