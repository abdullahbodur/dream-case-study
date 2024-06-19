import os
import time
import subprocess
import requests
import json
import argparse

JMETER_VERSION = '5.6.3'

parser = argparse.ArgumentParser(description='Run JMeter tests')
parser.add_argument('--environment', type=str, default='local', help='Environment to run the tests')
parser.add_argument('--jmeter_home', type=str, default='/opt/apache-jmeter-5.4.1', help='JMeter home directory')
parser.add_argument('--project_dir', type=str, help='Project directory')
parser.add_argument('--report_dir', type=str, help='Report directory')
parser.add_argument('--service-host', type=str, default='localhost', help='Service host')
args = parser.parse_args()

JMETER_HOME = os.getenv('JMETER_HOME')

if args.project_dir is not None:
  os.chdir(args.project_dir)
else:
  os.chdir(os.path.join(os.path.dirname(os.path.realpath(__file__)), '..'))

current_dir=os.getcwd()
plan_dir = os.path.join(current_dir, 'app-performance-test', 'src', 'test', 'jmeter')
if args.report_dir is not None:
  report_dir = args.report_dir
else:
  report_dir = os.path.join(current_dir, 'deployment', 'performance-test', 'reports')

if args.environment == 'local':
  # current_dir has jmeter directory
  if os.path.exists(current_dir + '/apache-jmeter-' + JMETER_VERSION):
    JMETER_HOME = current_dir + '/apache-jmeter-' + JMETER_VERSION
  elif JMETER_HOME is None or not os.path.exists(JMETER_HOME):
    print("Installing JMeter")
    subprocess.run(['wget', f'https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-{JMETER_VERSION}.tgz'])
    subprocess.run(['tar', '-xvf', f'apache-jmeter-{JMETER_VERSION}.tgz'])
    subprocess.run(['mv', f'apache-jmeter-{JMETER_VERSION}', current_dir])
    subprocess.run(['rm', f'apache-jmeter-{JMETER_VERSION}.tgz'])
    JMETER_HOME = os.path.join(current_dir, f'apache-jmeter-{JMETER_VERSION}')
  # maven clean install
  print("Running maven clean install")
  subprocess.run(['mvn', 'clean', 'install', '-DskipTests'])
  # copy jar to jmeter lib
  print("Copying jar to JMeter lib directory")
  # jar name might be different so check all jars in target directory
  for file in os.listdir(f'{current_dir}/app-performance-test/target'):
    if file.endswith('.jar'):
        subprocess.run(['cp', f'{current_dir}/app-performance-test/target/{file}', f'{JMETER_HOME}/lib/junit/'])

  # set environment variables
  DATABASE_URL = 'jdbc:mysql://localhost:3306/mysql-db'
  DATABASE_USER = 'username'
  DATABASE_PASSWORD = 'password'
  BASE_URL = 'http://localhost:8080'
  os.environ['DATABASE_URL'] = DATABASE_URL
  os.environ['DATABASE_USER'] = DATABASE_USER
  os.environ['DATABASE_PASSWORD'] = DATABASE_PASSWORD
  os.environ['BASE_URL'] = BASE_URL
else:
  BASE_URL = os.getenv('BASE_URL')

# Load test configurations from configuration.json
with open(os.path.join(plan_dir, 'configuration.json')) as f:
    test_configurations = json.load(f)

print(f"Running {len(test_configurations)} JMeter tests")
# create results directory
print(f"Creating {report_dir}")
print(f"Removing existing files in {report_dir}")
os.makedirs(report_dir, exist_ok=True)

subprocess.run(['rm', '-rf', report_dir])

# For each test configuration
for test_config in test_configurations:
    # wait until http://backend-engineering-case-study:8080/status returns 200
    while True:
        response = requests.get(f'{BASE_URL}/status')
        if response.status_code == 200:
            break
        print("Waiting for http://backend-engineering-case-study:8080/status to return 200")
        time.sleep(1)

    print("Service is up and running")
    print(f"Running JMeter test {test_config['testName']}")
    print(f"Class Name: {test_config['className']}")
    print(f"Method Name: {test_config['methodName']}")
    print(f"Thread Count: {test_config['threadCount']}")
    print(f"Loop Count: {test_config['loopCount']}")
    print(f"Duration: {test_config['duration']}")

    # create test results directory
    os.makedirs(f'{report_dir}/{test_config["testName"]}', exist_ok=True)

    subprocess.run([
        f'{JMETER_HOME}/bin/jmeter.sh', '-n',
        '-t', f'{current_dir}/app-performance-test/src/test/jmeter/PerformanceTestPlan.jmx',
        f"-JCLASS_NAME={test_config['className']}",
        f"-JMETHOD_NAME={test_config['methodName']}",
        f"-JTHREADS={test_config['threadCount']}",
        f"-JLOOP_COUNT={test_config['loopCount']}",
        f"-JDURATION={test_config['duration']}",
        f"-JTEST_DESCRIPTION={test_config['testDescription']}",
        '-l', f"{report_dir}/{test_config['testName']}/{test_config['testName']}.jtl",
        '-e', '-o', f"{report_dir}/{test_config['testName']}"
    ])

    subprocess.run(['mv', f'{current_dir}/jmeter.log', f'{report_dir}/{test_config["testName"]}'])
    print(f"JMeter test {test_config['testName']} completed")

if args.environment == 'local':
  # stop mysql
  # remove environment variables
  os.environ.pop('DATABASE_URL')
  os.environ.pop('DATABASE_USER')
  os.environ.pop('DATABASE_PASSWORD')
  os.environ.pop('BASE_URL')

print("JMeter test completed successfully")

# run forever to keep the container running
while True:
  time.sleep(1)