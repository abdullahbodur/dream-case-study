cd $JMETER_HOME/bin

## wait until http://app:8080/status returns 200
while true; do
  curl -s -o /dev/null -w "%{http_code}" http://app:8080/status
  if [ $? -eq 0 ]; then
    break
  fi
  echo "Waiting for http://app:8080/status to return 200"
  sleep 1
done

# create results directory
mkdir -p /reports/jmeter
rm -rf /reports/jmeter/*
./jmeter.sh -n \
  -t $JMETER_HOME/plans/jmeter/PerformanceTestPlan.jmx \
  -l /reports/jmeter/PerformanceTestPlan/PerformanceTestPlan.jtl \
  -e -o /reports/jmeter/PerformanceTestPlan

# copy jmeter.log to /reports/jmeter/PerformanceTestPlan
cp jmeter.log /reports/jmeter/PerformanceTestPlan

echo "JMeter test completed"
while true; do
  sleep 1
done