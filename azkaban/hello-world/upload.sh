#!/usr/bin/env bash
#打包
cd  /e/github/java/azkaban/hello-world && 7z a -tzip hello.zip *.flow *.project || exit
#上传
curl -i -X POST --form 'session.id=ae9904e5-6c49-45bc-bd83-7b302d7b0cae' --form 'ajax=upload' --form 'file=@hello.zip;type=application/zip' --form 'project=hello' http://hadoop100:8081/manager
#运行
curl -XPOST --data 'session.id=ae9904e5-6c49-45bc-bd83-7b302d7b0cae' --data 'ajax=executeFlow' --data 'project=hello' --data 'flow=java' http://hadoop100:8081/executor
