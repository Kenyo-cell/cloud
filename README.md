# Cloud Api Backend Application
___
## Description
This application made with Netology Group Java Backend Dev Course 
like Diploma Project.

The Application is RESTfull backend provided API described with OpenAPI specification
in [this document](./cloud-api.yaml)

### Dependencies
This Cloud Api Project uses:
* Spring Boot Starter Web
* Spring Boot Starter Data JPA
* Spring Boot Starter Security
* Spring Boot Starter OAuth2 Resource Server
* Spring Boot Starter Test
* Lombok
* Jackson FasterXML
* Liquibase
* TestContainers
* H2 In Memory Database
___
## Installation
Use `git clone` with master branch of this project

You also need to install or get the last version of Docker and Docker Compose.

___
## Configuration 
This application besides default Spring Boot application.yml configuration file
uses external .env file configuration and imports at start of the Application.

So firstly you need to override (or just agree) [env.properties](src/main/resources/env.properties) file.
Check and set parameters for db connection:

```text
DB_USER=%your user%
DB_PASS=%your user pass%
DB_DB=%name of root db%
DB_URL=%need no change%
```

Second step is define your own `*.pem` files as ssh keys.
My configuration is just an example, and you can use example keys
located in classpath resource directory.
___
## Running
This Application has 2 docker-compose files:
1. clean rest build
2. build with frontend application implementation

Firstly, you need to choose this one you want to try:
with REST client like Postman to send HTTP queries
or with browser GUI implementation.

So after choosing your way you need to run:
1. `docker-compose -f rest-docker-compose up`
2. `docker-compose -f front-docker-compose up`

First only and easy way to use the application is to login with
(Login: ADMIN; PASSWORD=ADMIN).
