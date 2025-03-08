# Getting Started

## How to run

### Locally

* Build the project:
```
./mvnw clean package
```
Build skipping tests
```
./mvnw clean package -DskipTests
```
* Execute the project
```
java -jar target/5e.exptracker-0.0.1-SNAPSHOT.jar
```

- To use [Docker](https://www.docker.com) execute this command, MySQL will start after that
```
docker-compose -f docker-compose.yml up -d
```

The API will be available at [localhost:8080](http://localhost:8080).

Swagger will be available at [localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
