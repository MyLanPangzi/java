#Docker学习笔记
##常用命令
###帮助命令
* info
* version
* --help
###镜像命令
* search
* pull
* push
* images
* rmi
* tag
* commit
* build
* history
* inspect
###容器命令
* ps
* rm 
* attach
* exec
* logs
* cp
* create
* inspect
* pause
* unpause
* run
* stop
* start
* restart
* wait
* kill
* top

##镜像
* 联合文件系统，共享资源
* commit
* Union FS Union文件系统（UnionFS）是一种分层、轻量级并且高性能的文件系统，<br>
它支持对文件系统的修改作为一次提交来一层层的叠加，<br>
同时可以将不同目录挂载到同一个虚拟文件系统下(unite several directories into a single virtual filesystem)。<br>
Union 文件系统是 Docker 镜像的基础。<br>
镜像可以通过分层来进行继承，基于基础镜像（没有父镜像），可以制作各种具体的应用镜像。
* Boot FS
* Root FS
* Container layer
* Image layer

##数据卷
* docker run -v --volumes-from
* 持久化数据
* 共享数据
* 数据卷容器，继承父容器数据卷，多容器共享数据卷
##Dockerfile
* 镜像的定义，（源码）
##常用应用
* nginx
* redis
* mongo
* mysql
* tomcat

##发布到阿里云
* login
* tag
* push