# Backend Engineering Case Study

![Coverage](.github/badges/jacoco.svg)
![Branches](.github/badges/branches.svg)
![Build](https://github.com/abdullahbodur/dream-case-study/actions/workflows/build.yaml/badge.svg)

### Introduction
This project is a simple backend application that is created for the Dream Case Study.

### Redis Methodology

With redis methodology, microservice is capable of holding high throughput and decreased latency on
related features

#### Realtime Group Pool Mechanism

With in the live tournament, we hold hot group details (empty slots, group id, group name, etc.)
in redis. When a user wants to join a tournament, we can easily find the available group with the
user's
country. With this way, we can decrease the latency of the system.

#### Live Ranking Mechanism.

With the live ranking mechanism, we can easily find the user's ranking in the leaderboard with
respect to the user's country and group.

#### Fast Updated Level Details with Redis.

With the updated level details, application updates the user's level details in the redis. With this
way, we can easily re rank the leaderboard of countries and related group with respect to the user's
current score.

#### Note that

All tournament related data is stored in the MySQL database. Redis is used for the live data that.
After / with the tournament, application updates the MySQL database with the final results.
After tournament, application clears the redis data for the next tournament.

### How to run the application

To run the application, you need to have docker and docker-compose installed on your machine.
You can Install them via the following links:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

After installing docker and docker-compose, you can run the following command to start the
application

```shell
cd deployment && docker-compose up -d
```

### Diagrams

#### Docker Compose Diagram

```mermaid
C4Deployment
    title System Context diagram for Docker Compose
    Enterprise_Boundary(b0, "local-network") {
        Deployment_Node(redis, "Redis") {
            Container(redis, "Redis", "In Memory Database", "Provides in-memory database for tournament and user progress data")
        }

        Deployment_Node(mysqldb, "MySQL") {
            Container(mysqldb, "MySQL", "Relational Database", "Provides relational database for user and tournament data")
        }

        Deployment_Node(phpmyadmin, "PhpMyAdmin") {
            Container(phpmyadmin, "PhpMyAdmin", "UI for Database", "Provides web interface for managing MySQL database")
        }

        Deployment_Node(redisinsight, "RedisInsight") {
            Container(redisinsight, "RedisInsight", "UI for Redis", "Provides web interface for managing and monitoring Redis")
        }

        Deployment_Node(app, "Spring Boot") {
            Container(app, "Spring Boot", "Java Application", "Provides backend services for the application")
        }

        Rel(app, mysqldb, "Connects to")
        Rel(phpmyadmin, mysqldb, "Manages")
        Rel(app, redis, "Connects to")
        Rel(redisinsight, redis, "Manages and monitors")
    }
```

#### Sequence Diagram for Enter Tournament

```mermaid
sequenceDiagram
    participant U as User
    participant A as Application
    participant UserProgressService as UserProgressService
    participant TournamentService as TournamentService
    participant LeaderboardService as LeaderboardService
    participant RewardService as RewardService
    participant GroupPoolService as GroupPoolService
    participant GroupService as GroupService
    participant ParticipantService as ParticipantService
    participant M as MySQL
    participant R as Redis
    U ->> A: Enter Tournament
    A ->> UserProgressService: Get User Progress
    UserProgressService ->> M Query: Get User Progress
    M ->> UserProgressService: User Progress
    UserProgressService ->> A: User Progress
    A ->> UserProgressService: Check Minimum Requirements
    UserProgressService ->> A: Minimum Requirements
    A ->> GroupService: Enter Tournament
    GroupService ->> RewardService: Check unclaimed rewards
    RewardService ->> M Query: Get unclaimed rewards
    M ->> RewardService: Unclaimed rewards
    RewardService ->> GroupService: Unclaimed rewards
    GroupService ->> LeaderboardService: Get joined group [already]
    LeaderboardService ->> R Query: joined group id
    R ->> LeaderboardService: joined group id
    LeaderboardService ->> GroupPoolService: Get available groups
    GroupPoolService ->> R Query: Get available groups
    R ->> GroupPoolService: Available groups
    GroupPoolService ->> GroupService: Available groups
    GroupService ->> R Transaction: Create new group
    GroupService ->> M Transaction: Create new group
    M ->> GroupService: New group
    ParticipantService ->> M Transaction: Add participant to group
    M ->> ParticipantService: Participant added
    ParticipantService ->> GroupService: Participant added to group
    GroupService ->> A: group created
    A ->> LeaderboardService: Create / Update leaderboard
    LeaderboardService ->> R Transaction: Create / Update leaderboard
    R ->> LeaderboardService: Leaderboard Updated / Created
    LeaderboardService ->> A: Leaderboard Updated / Created
    A ->> UserProgressService: Withdraw required points
    UserProgressService ->> M Transaction: Withdraw required points
    M ->> UserProgressService: Points Withdrawn
    UserProgressService ->> A: Points Withdrawn
    A ->> U: Success
```

### Postman Collection

You can find the postman collection [here](postman/dream_case_study.postman_collection.json).

### TODO

- [x] Create a simple spring boot application that creates table in the database
- [ ] Add custom exception handling
    - [X] Add custom exception handling for the application
    - [X] Refactor error responses for the application
- [X] Create more complex structure for the application
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
        - [X] Add plantuml diagrams for the project
        - [X] Add how to run the application
    - [X] Write javadoc for the functions in the project
- [ ] Optimize the code for better performance
    - [X] Update Level Up request with redis that we can store updated live data without waiting too
      much time.
    - [X] Add live leaderboard with respect to given scorings. (Consider by Country and Group)
    - [ ] Add a queue for processing the requests