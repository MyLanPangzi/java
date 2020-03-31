#!/usr/bin/env bash
#打包
cd  /e/github/java/azkaban/hello-world && 7z a -tzip hello.zip *.flow *.project || exit
cd  /e/github/java/azkaban/hello-world && 7z a -tzip hello.zip *.job || exit
#上传
curl -k -X POST --data "action=login&username=admin&password=admin" https://hadoop102:8443
curl -k -i -X POST --form 'session.id=d49e5094-40e0-4087-836f-a98373a4113a' \
--form 'ajax=upload' \
--form 'file=@hello.zip;type=application/zip' \
--form 'project=hello' https://hadoop102:8443/manager

curl -i -X POST --form 'session.id=75f04c37-566c-4fbd-8982-f30d4215d48c' \
--form 'ajax=upload' \
--form 'file=@hello.zip;type=application/zip' \
--form 'project=hello' http://hadoop100:8081/manager
#运行
curl -XPOST --data 'session.id=75f04c37-566c-4fbd-8982-f30d4215d48c' \
--data 'ajax=executeFlow' \
--data 'project=hello' \
--data 'flow=java' http://hadoop100:8081/executor
curl  -X POST --data "action=login&username=azkaban&password=azkaban" http://hadoop100:8081/
curl --get \
--data "session.id=75f04c37-566c-4fbd-8982-f30d4215d48c&ajax=fetchFlowExecutions&project=hello&flow=test&start=0&length=3" http://hadoop100:8081/manager
curl --data "session.id=75f04c37-566c-4fbd-8982-f30d4215d48c&ajax=fetchexecflow&execid=304" http://localhost:8081/executor

