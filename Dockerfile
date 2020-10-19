FROM maven:3.6.3-jdk-11 AS build

# copy the project files
COPY ./pom.xml ./pom.xml

# build all dependencies for offline use
RUN mvn dependency:go-offline -B

# copy your other files
# .git folder needs for providing info for actuator
COPY ./.git ./.git
COPY ./src ./src

# build for release
RUN mvn install -Dmaven.test.skip=true

FROM amazoncorretto:11

# set deployment directory
WORKDIR /user-service

# copy over the built artifact from the maven image
COPY --from=build target/user-service-*.jar ./user-service.jar

# set the startup command to run your binary
CMD ["java", "-jar", "./user-service.jar"]
