# Backend Engineering Case Study

Add brief explanation of how you organized your implementation and the choices you made in terms of design while solving problems


### Redis Methodology 
With redis methodology, microservice is capable of holding high throughput and decreased latency on related features 
#### Hot Waiting Room for New Tournament Groups

#### Live Ranking Mechanism.

#### Updated Level Details with Redis.


### TODO
- [x] Create a simple spring boot application that creates table in the database 
- [ ] Add custom exception handling
- [ ] Create more complex structure for the application
- [ ] Write unit tests for the functions in `src/` directory
- [ ] Write github workflow to run the tests
- [ ] Optimize dockerfile for the application
- [ ] Optimize the code for better performance
  - [ ] Update Level Up request with redis that we can store updated live data without waiting too much time.
  - [ ] Add live leaderboard with respect to given scorings. (Consider by Country and Group)
  - [ ] Add a queue for processing the requests