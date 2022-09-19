[![Java CI with Maven](https://github.com/DenisYakovinov/PassportService/actions/workflows/maven.yml/badge.svg)](https://github.com/DenisYakovinov/PassportService/actions/workflows/maven.yml)
# passport service

This application is a service for managing passports.<br>
It is possible to add, edit, update and view passport data via REST API<br>
The service is responsible for checking the expired passports.<br>
It periodically checks for expired passports and, if any, sends<br>
a request through a message broker to the mail distribution service,<br>
which already sends messages to the mail that the passport is<br>
expired and needs to be replaced.<br>

/save, save passport data<br>
/update?id=*, update passport data<br>
/delete?id=*, delete passport data<br>
/find, upload all passports<br>
//find?seria=*, upload passports with the specified series<br>
//unavaliable, upload passports whose expiration date has<br>
expired /find-replaceable, upload passports that need to be replaced in the next 3 months

The second service calls the methods of the first using RestTemplate

The service runs a scheduled passport check, which expires and sends a message<br>
to the Apache Kafka broker, which forwards it to the consumer on the same service<br>
and displays a message in the log.<br>
Instead of a log, for example, you can send an Email

To run the app (need at least java 11), clone the project
```
https://github.com/DenisYakovinov/PassportService.git
```
then using terminal from root directory:<br>

1. run db, kafka broker, zookeeper in docker
```
docker-compose up 
```
2. then
```
mvn install
```
3. and run
```
java -jar target/PassportRestService-0.0.1-SNAPSHOT.jar
```
4. or
```
mvn spring-boot:run
```
