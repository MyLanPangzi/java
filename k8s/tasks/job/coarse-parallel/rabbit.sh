#!/usr/bin/env bash
kubectl run -i --tty temp --image ubuntu:18.04 && sleep 20
apt-get update && apt-get install -y curl ca-certificates amqp-tools python dnsutils
nslookup rabbitmq-service
env | grep RABBIT | grep HOST
export BROKER_URL=amqp://guest:guest@rabbitmq-service:5672
/usr/bin/amqp-declare-queue --url=$BROKER_URL -q foo -d
/usr/bin/amqp-publish --url=$BROKER_URL -r foo -p -b Hello
/usr/bin/amqp-consume --url=$BROKER_URL -q foo -c 1 cat && echo
/usr/bin/amqp-declare-queue --url=$BROKER_URL -q job1  -d
for f in apple banana cherry date fig grape lemon melon
do
  /usr/bin/amqp-publish --url=$BROKER_URL -r job1 -p -b $f
done
exit

tee woker.py <<-EOF
#!/usr/bin/env python

# Just prints standard out and sleeps for 10 seconds.
import sys
import time
print("Processing " + sys.stdin.readlines()[0])
time.sleep(10)
EOF
chmod +x worker.py

docker build -t registry.cn-hangzhou.aliyuncs.com/twocat/job-wq-1 .
docker image push registry.cn-hangzhou.aliyuncs.com/twocat/job-wq-1

