Cake Manager
============

# Prerequisites

* Requires Java 11 
* Developed using: openjdk version "11.0.6" 2020-01-14

#How to run

* `./gradlew build`
* `java -jar build/libs/cake-manager-0.0.1-SNAPSHOT.jar`

# Interfaces

The service exposes:
* a REST API supporting creation and retrieval of cakes (see CakeRestController): http://localhost:8282/cakes
* a web (Thymeleaf) UI (see CakeUiController): http://localhost:8282/
* Interactive swagger documentation on: http://localhost:8282/swagger-ui.html

# Notes

Assumptions:
* Given the limited functionality in the original servlet-based application it is simpler to start from scratch in a new 
Spring Boot based codebase, rather than try to add features to an immediately redundant codebase
* Loading cakes JSON via GitHub URL was a temporary measure (and has been removed from this new implementation) 
* App is small and so integration tests (ApplicationTest.java) provide good enough coverage - as the application
gets larger component/unit tests will be required
* Pagination is not required - depending on the number of cakes expected this may or may not be required

Given more time (I've implemented the below on various projects previously):
* Dockerising the application
* Running the build in CI (e.g. Jenkins/TeamCity)
* Code quality metrics (test coverage, FindBugs, CheckStyle, etc) using SonarQube
* Tests for Web UI in a tool like Selenium
* Automated deployment
* End-to-end integration tests in deployed environment
* More comprehensive /health endpoint which tests upstream dependencies' (e.g. database) availability