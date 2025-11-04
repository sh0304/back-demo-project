# Build stage
FROM gradle:8.5-jdk17 AS build
ARG PROFILE
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN gradle build -Pprofile=${PROFILE} --no-daemon

# Package stage
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
