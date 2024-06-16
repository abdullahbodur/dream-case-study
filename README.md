# Backend Engineering Case Study

Add brief explanation of how you organized your implementation and the choices you made in terms of
design while solving problems

### Redis Methodology

With redis methodology, microservice is capable of holding high throughput and decreased latency on
related features

#### Hot Waiting Room for New Tournament Groups

#### Live Ranking Mechanism.

#### Updated Level Details with Redis.

### TODO

- [x] Create a simple spring boot application that creates table in the database
- [ ] Add custom exception handling
    - [X] Add custom exception handling for the application
    - [ ] Refactor error responses for the application
- [ ] Create more complex structure for the application
- [ ] Write unit tests for the functions in `src/` directory
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