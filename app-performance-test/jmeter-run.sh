# create results directory
mkdir -p /reports/jmeter
rm -rf /reports/jmeter/*
jmeter -n \
  -t $JMETER_HOME/plans/jmeter/PerformanceTestPlan.jmx \
  -l /reports/jmeter/PerformanceTestPlan/PerformanceTestPlan.jtl \
  -e -o /reports/jmeter/PerformanceTestPlan

# copy jmeter.log to /reports/jmeter/PerformanceTestPlan
cp $JMETER_HOME/jmeter.log /reports/jmeter/PerformanceTestPlan
while true; do
  sleep 1
done