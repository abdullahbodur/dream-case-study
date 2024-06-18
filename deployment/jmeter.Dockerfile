FROM maven:3.8.8-eclipse-temurin-17 AS Builder
ARG PROJECT_NAME
WORKDIR /project
COPY .. .
RUN mvn clean package --projects $PROJECT_NAME --also-make

# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17

ARG PROJECT_NAME
# Set the JMeter version
ENV JMETER_VERSION 5.6.3

# Download and install JMeter
RUN wget https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-${JMETER_VERSION}.tgz && \
    tar -xvzf apache-jmeter-${JMETER_VERSION}.tgz && \
    rm apache-jmeter-${JMETER_VERSION}.tgz

# Set JMeter home
ENV JMETER_HOME /apache-jmeter-${JMETER_VERSION}/

# Add JMeter to the PATH
ENV PATH $JMETER_HOME/bin:$PATH

# Copy your test plan to the Docker image
COPY --from=Builder /project/$PROJECT_NAME/src/test $JMETER_HOME/plans/
COPY --from=Builder /project/$PROJECT_NAME/target/*.jar $JMETER_HOME/lib/junit/
COPY --from=Builder /project/$PROJECT_NAME/jmeter-run.sh $JMETER_HOME/jmeter-run.sh

# Set the working directory
WORKDIR $JMETER_HOME
RUN chmod +x jmeter-run.sh
# run jmeter-run.sh
CMD ["/bin/bash", "-c", "./jmeter-run.sh"]