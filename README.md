# CRM Service
CRM Service is a service that provides the functionality to manage customers and users.

You have to install the following software to run the application:

- [Java 19] (http://www.oracle.com/technetwork/java/javase/downloads)
- [Maven] (https://maven.apache.org)
- [MySQL] (https://www.mysql.com)
- [Docker] (https://www.docker.com)

## Development

AWS credentials are required to run the application. You can set the credentials in the following environment variables:

```
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
```

You'll have to run a MySQL instance in your local machine or in a docker container. You can use the following command to run a MySQL instance in a docker container:

```
docker run --name mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=crm -e MYSQL_USER=crm -e MYSQL_PASSWORD=crm -p 3306:3306 -d mysql:5.7
```

configure the database connection in the `application.yml` file or as environment variables:

```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/crm?useSSL=false&useUnicode=yes&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
    username: crm
    password: crm
```

To start your application in the default profile, run:

```
./mvnw install
```

And then run the application:

```
./mvnw spring-boot:run
```

## Building for production

To build the final war and optimize the crm application for production, run:

```
./mvnw clean package
```

To ensure everything worked, run:

```
java -jar target/*.war
```
You should configure the database connection using the environment variables:

```
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

## Docker

To build the docker image, run:

```
mvn compile jib:dockerBuild
```

To run the docker image, run:

```
docker run -p 8080:8080 crm-service -d crm-service
```

