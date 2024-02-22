FROM openjdk:21-slim-bullseye AS build

WORKDIR /home/app

COPY gradlew /home/app/
COPY gradle /home/app/gradle
COPY build.gradle /home/app/
COPY settings.gradle /home/app/

COPY src /home/app/src

RUN chmod +x ./gradlew

RUN ./gradlew build --no-daemon

FROM openjdk:21-slim-bullseye AS runtime

# Copy the built jar from the build stage to the runtime container
COPY --from=build /home/app/build/libs/*-fatjar.jar /app/app.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
