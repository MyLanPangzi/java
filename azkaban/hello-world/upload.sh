#!/usr/bin/env bash
#打包
cd  /e/github/java/azkaban/hello-world && 7z a -tzip hello.zip *.flow *.project || exit
#上传
curl -i -X POST --form 'session.id=5d255b9e-23eb-4de3-9aa1-1db377941b51' --form 'ajax=upload' --form 'file=@hello.zip;type=application/zip' --form 'project=hello' http://hadoop100:8081/manager
#运行
curl -XPOST --data 'session.id=5d255b9e-23eb-4de3-9aa1-1db377941b51' --data 'ajax=executeFlow' --data 'project=hello' --data 'flow=java' http://hadoop100:8081/executor
curl  -X POST --data "action=login&username=azkaban&password=azkaban" http://hadoop100:8081/
curl --get --data "session.id=5d255b9e-23eb-4de3-9aa1-1db377941b51&ajax=fetchFlowExecutions&project=hello&flow=test&start=0&length=3" http://hadoop100:8081/manager
curl --data "session.id=5d255b9e-23eb-4de3-9aa1-1db377941b51&ajax=fetchexecflow&execid=304" http://localhost:8081/executor

