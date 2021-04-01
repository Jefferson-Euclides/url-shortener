# **URL Shortener**
## Description
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

## Mandatory Requirements
- Design and implement an API for short URL creation
- Implement forwarding of short URLs to the original ones
- There should be some form of persistent storage

## Technologies
- Java 8
- Spring Boot 2.4.4
- Spring Boot Actuator
- H2
- REST API
- JUnit and Mockito

## How to compile and run the application with an example for each call
Install: git clone https://github.com/Jefferson-Euclides/url-shortener/tree/master

- Build: mvn package
- Deploy: mvn spring-boot:run

## Examples for each call
POST http://localhost:8080/?url={url-to-be-shortened} - Saves the URL and return the shortened version

GET localhost:8080/short/{encrypted-url} - This will be the return of the first one

GET localhost:8080/metrics - To get the access metrics of all URL's