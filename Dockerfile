FROM arm64v8/maven:3.8.6-eclipse-temurin-17
WORKDIR ./app
COPY ./ .
RUN mvn clean install
CMD [ "mvn", "spring-boot:run" ]
