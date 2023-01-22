# Start with a base image containing Java runtime
FROM openjdk:17

# Add Maintainer Info

# Add a volume pointing to /tmp
VOLUME /tmp

EXPOSE 8091


RUN mkdir /app
WORKDIR /app
COPY . .
RUN ./gradlew build


ENTRYPOINT ["java","-jar","./build/libs/user-service-1.0.jar","--spring.config.location=classpath:/docker.yaml"]