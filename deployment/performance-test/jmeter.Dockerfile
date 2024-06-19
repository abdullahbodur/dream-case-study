FROM maven:3.8.8-eclipse-temurin-17 AS Builder
ARG PROJECT_NAME
WORKDIR /project
COPY ../../.. .
RUN mvn clean install --projects $PROJECT_NAME --also-make

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

# install python libraries
RUN apt-get update && apt-get install -y python3 python3-pip
RUN pip3 install requests

WORKDIR /app
COPY ../../.. .

# Copy your test plan to the Docker image
COPY --from=Builder /project/$PROJECT_NAME/target/*.jar $JMETER_HOME/lib/junit/
# Set the working directory
WORKDIR /app/scripts
# run jmeter-run.sh
CMD ["/bin/bash", "-c", "python3 jmeter_run.py --environment pipeline"]