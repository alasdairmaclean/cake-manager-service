Cake Manager
============

# Prerequisites

* Requires Java 11 
* Developed using: openjdk version "11.0.6" 2020-01-14

#How to run

* `./gradlew build`
* `java -jar build/cake-manager-0.0.1-SNAPSHOT.jar`

# Interfaces

The service exposes:
* a web (Thymeleaf) UI on http://localhost:8282/ (see CakeUiController)
* a REST API supporting creation and retrieval of cakes via http://localhost:8282/cakes (see CakeRestController)

Swagger documentation can be found under http://localhost:8282/swagger-ui.html

# Notes

Assumptions:
* Given the limited functionality in the original servlet-based application it is simpler to start from scratch in a new 
Spring Boot based codebase, rather than try to add features to an immediately redundant codebase
* Loading cakes JSON via GitHub URL was a temporary measure (and has been removed from the production version) 
* App is small and so integration tests (ApplicationTest.java) can provide good enough coverage - as the application
gets larger component/unit tests will be required

Possible improvements:
* Error handling in Web UI could be improved upon
* Tests for Web UI - best done with a tool like Selenium but not yet implemented
* Pagination is not (yet) implemented - depending on the number of cakes expected this may or may not be required