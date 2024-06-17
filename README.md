# Backend Engineering Case Study

![Coverage](.github/badges/jacoco.svg)
![Branches](.github/badges/branches.svg)
![Build](https://github.com/abdullahbodur/dream-case-study/actions/workflows/build.yaml/badge.svg)

Add brief explanation of how you organized your implementation and the choices you made in terms of
design while solving problems

### Redis Methodology

With redis methodology, microservice is capable of holding high throughput and decreased latency on
related features

#### Hot Waiting Room for New Tournament Groups

#### Live Ranking Mechanism.

#### Updated Level Details with Redis.

```mermaid
C4Deployment
title System Context diagram for Docker Compose
Enterprise_Boundary(b0, "local-network") {

    Deployment_Node(redis, "Redis"){
        Container(redis, "Redis", "In Memory Database", "Provides in-memory database for tournament and user progress data")
    }
    
    Deployment_Node(mysqldb, "MySQL"){
        Container(mysqldb, "MySQL", "Relational Database", "Provides relational database for user and tournament data")
    }
    
    Deployment_Node(phpmyadmin, "PhpMyAdmin"){
        Container(phpmyadmin, "PhpMyAdmin", "UI for Database", "Provides web interface for managing MySQL database")
    }
    
    Deployment_Node(redisinsight, "RedisInsight"){
        Container(redisinsight, "RedisInsight", "UI for Redis", "Provides web interface for managing and monitoring Redis")
    }
    
    Deployment_Node(app, "Spring Boot"){
        Container(app, "Spring Boot", "Java Application", "Provides backend services for the application")
    }
    
    Rel(app, mysqldb, "Connects to")
    Rel(phpmyadmin, mysqldb, "Manages")
    Rel(app, redis, "Connects to")
    Rel(redisinsight, redis, "Manages and monitors")
}
```

### TODO

- [x] Create a simple spring boot application that creates table in the database
- [ ] Add custom exception handling
    - [X] Add custom exception handling for the application
    - [ ] Refactor error responses for the application
- [ ] Create more complex structure for the application
- [X] Write unit tests for the functions in `src/` directory
- [ ] Write github workflow to run the tests
    - [X] Add mvn unit tests to the github workflow
    - [X] Add code coverage to the github workflow
    - [ ] Add performance tests to the github workflow
- [ ] Optimize dockerfile for the application
- [ ] Write documentation for the project
    - [ ] Write README.md for the project
        - [ ] Add brief explanation of how you organized your implementation and the choices you
          made in terms of
          design while solving problems.
        - [ ] Add plantuml diagrams for the project
        - [ ] Add how to run the application
        - [ ] Add how to run the tests
        - [ ] Add features of the application
    - [ ] Write javadoc for the functions in the project
- [ ] Optimize the code for better performance
    - [X] Update Level Up request with redis that we can store updated live data without waiting too
      much time.
    - [X] Add live leaderboard with respect to given scorings. (Consider by Country and Group)
    - [ ] Add a queue for processing the requests