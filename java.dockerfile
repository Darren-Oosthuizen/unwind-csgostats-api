FROM maven:3.6.3-adoptopenjdk-8 AS base
WORKDIR /app
EXPOSE 8080

COPY src src
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY testdb.mv.db .
COPY testdb.trace.db .


FROM base AS build
RUN mvn clean install

FROM build as final
RUN chmod +x mvnw

CMD ./mvnw spring-boot:run
