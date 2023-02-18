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

To start your application in the default profile, run:

```
./mvnw install
```

```
./mvnw
```

## Building for production

```
./mvnw clean compile
```
