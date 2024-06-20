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

#### Project Organization Diagram

```mermaid
graph LR
    Project[Dream Case Study]
    PerformanceTesting[Application Performance Testing]
    A[App]
    B[api]
    C[common]
    D[configuration]
    E[enumaration]
    F[exceptions]
    G[tournament]
    H[user]
    I[resources]
    Project --> A
    Project --> PerformanceTesting
    A --> B
    A --> C
    A --> D
    A --> E
    A --> F
    A --> G
    A --> H
    A --> I
    B --> BA[StatusController.java]
    B --> BB[dto]
    BB --> BBA[request]
    BB --> BBB[response]
    C --> CA[utils]
    C --> CB[validation]
    G --> GA[controller]
    G --> GB[entity]
    G --> GC[repository]
    G --> GD[service]
    H --> HA[controller]
    H --> HB[entity]
    H --> HC[repository]
    H --> HD[service]
    I --> IA[application-test.yml]
    I --> IB[application.yml]
    I --> IC[db]
```

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

### Performance Testing

On each commit, the application is tested with the performance tests. You can find the performance
test workflow [here](.github/workflows/build.yaml).
After each performance test workflow run, it generates simplified overviews of the performance tests
as tables:

#### UpdateLevelUser

| transaction            | sampleCount | errorCount | errorPct | meanResTime       | medianResTime | minResTime | maxResTime | pct1ResTime       | pct2ResTime        | pct3ResTime | throughput        | receivedKBytesPerSec | sentKBytesPerSec |
|------------------------|-------------|------------|----------|-------------------|---------------|------------|------------|-------------------|--------------------|-------------|-------------------|----------------------|------------------|
| Update level of a user | 400         | 0          | 0.0      | 54.60500000000003 | 38.0          | 5.0        | 312.0      | 79.90000000000003 | 301.49999999999807 | 311.99      | 306.5134099616858 | 0.0                  | 0.0              |
| Total                  | 400         | 0          | 0.0      | 54.60500000000003 | 38.0          | 5.0        | 312.0      | 79.90000000000003 | 301.49999999999807 | 311.99      | 306.5134099616858 | 0.0                  | 0.0              |

#### CreateNewUser

| transaction       | sampleCount | errorCount | errorPct | meanResTime       | medianResTime | minResTime | maxResTime | pct1ResTime       | pct2ResTime       | pct3ResTime | throughput        | receivedKBytesPerSec | sentKBytesPerSec |
|-------------------|-------------|------------|----------|-------------------|---------------|------------|------------|-------------------|-------------------|-------------|-------------------|----------------------|------------------|
| Total             | 400         | 0          | 0.0      | 77.23250000000004 | 58.0          | 7.0        | 421.0      | 98.90000000000003 | 393.2499999999971 | 420.0       | 228.8329519450801 | 0.0                  | 0.0              |
| Create a new user | 400         | 0          | 0.0      | 77.23250000000004 | 58.0          | 7.0        | 421.0      | 98.90000000000003 | 393.2499999999971 | 420.0       | 228.8329519450801 | 0.0                  | 0.0              |

Also it generates a detailed report for the performance test. You can find the detailed report from
the artifacts
section of the workflow.

<img src="docs/artifacts.png" alt="Artifacts" width="100%"/>

#### Detailed Report

<img src="docs/performance-test-detailed.png" alt="Detailed Report" width="100%"/>

#### configuration.json

In this file, I aimed to configure some basic jmeter parameters easily. Also, you can add a new test
by adding a new json object to the configuration.json file. You can find the related
file [here](app-performance-test/src/test/jmeter/configuration.json)

```json
[
  {
    "testName": "CreateNewUser",
    "testDescription": "Create a new user",
    "className": "com.dreamgames.backendengineeringcasestudy.PerformanceTest",
    "methodName": "createUserTest",
    "threadCount": 20,
    "loopCount": 20,
    "duration": 20
  },
  {
    "testName": "UpdateLevelUser",
    "testDescription": "Update level of a user",
    "className": "com.dreamgames.backendengineeringcasestudy.PerformanceTest",
    "methodName": "updateLevelTest",
    "threadCount": 20,
    "loopCount": 20,
    "duration": 20
  }
]
```

#### How to run the performance tests Locally

In case you want to run the performance tests locally, you can use the following command:

```shell
  cd deployment/performance-test
  docker compose up -d --build --force-recreate
```

It will create a simple non-gui docker environment for the performance tests. You can find the
related
docker-compose file [here](deployment/performance-test/docker-compose.yml). After the environment is
up
It will check the application with trying rest api calls over /status endpoint. You can find the
related
script [here](scripts/jmeter_run.py).

If you have already up and running docker environment. And, dont want to wait docker build stages.
Still, you can use jmeter_run.py script to run the performance tests. With parameterized
methodology:

If the running environment is local:

- defines user variables for the jmeter test
- builds the project with maven
- moves required jar files and configures with configuration.json file
- runs the jmeter tests with the given parameters
- creates a detailed report

```shell
  cd scripts
  python3 jmeter_run.py
```

If the running environment is github actions:

- fetches user variables
- configures the jmeter tests with the given parameters
- runs the jmeter tests with the given parameters
- creates a detailed report and saves it as proper artifact

### TODO

- [x] Create a simple spring boot application that creates table in the database
- [X] Add custom exception handling
    - [X] Add custom exception handling for the application
    - [X] Refactor error responses for the application
- [X] Create more complex structure for the application
- [X] Write unit tests for the functions in `src/` directory
- [X] Write github workflow to run the tests
    - [X] Add mvn unit tests to the github workflow
    - [X] Add code coverage to the github workflow
    - [X] Add performance tests to the github workflow
- [ ] Optimize dockerfile for the application
- [X] Write documentation for the project
    - [X] Write README.md for the project
        - [X] Add brief explanation of how you organized your implementation and the choices you
          made in terms of
          design while solving problems.
        - [X] Add plantuml diagrams for the project
        - [X] Add how to run the application
    - [X] Write javadoc for the functions in the project
- [X] Optimize the code for better performance
    - [X] Update Level Up request with redis that we can store updated live data without waiting too
      much time.
    - [X] Add live leaderboard with respect to given scorings. (Consider by Country and Group)

#### Notes

I did not focus on the security of the application. Because it was not mentioned in the case study.
However, I also asked for the security requirements of the application via email. Yet, I did not get
any response. So, I did not focus on the security of the application.

What I can add for the security of the application:

- [ ] Add SSO Service to the docker environment
- [ ] Enable Spring Security for the application
- [ ] Add Security Layers with Spring 3.0
- [ ] Implement an JWT Decoder & User Details Service for the application
- [ ] Cors Configuration for the application
- [ ] Add CSRF Protection for the application
- [ ] Add JWT Auth Converter for the application