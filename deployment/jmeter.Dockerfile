# Use an official OpenJDK runtime as a parent image
FROM openjdk:8

# Set the JMeter version
ENV JMETER_VERSION 5.4.1

ARG JAR_PATH

# Download and install JMeter
RUN wget https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-${JMETER_VERSION}.tgz && \
    tar -xvzf apache-jmeter-${JMETER_VERSION}.tgz && \
    rm apache-jmeter-${JMETER_VERSION}.tgz

# Set JMeter home
ENV JMETER_HOME /apache-jmeter-${JMETER_VERSION}/

# Add JMeter to the PATH
ENV PATH $JMETER_HOME/bin:$PATH

# Copy your test plan to the Docker image
COPY ../app-performance-test/src/test/jmeter/PerformanceTestPlan.jmx $JMETER_HOME
COPY $JAR_PATH $JMETER_HOME/junit/

# Default command will run the test plan
CMD ["jmeter", "-n", "-t", "/apache-jmeter-${JMETER_VERSION}/PerformanceTestPlan.jmx"]