FROM arm64v8/maven:3.8.6-eclipse-temurin-17
WORKDIR /application
ADD ./ /application/
RUN mvn clean install
CMD [ "mvn", "spring-boot:run" ]
