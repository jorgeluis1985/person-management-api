FROM eclipse-temurin:11-jdk-alpine as base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src

FROM base as test
RUN ./mvnw test

FROM base as development
CMD [ "./mvnw", "spring-boot:run" ]

FROM base as build
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:11-jre-alpine as production
EXPOSE 8080
COPY --from=build /app/target/person-management-*.jar /person-management.jar
CMD [ "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/person-management.jar" ]
