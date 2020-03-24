#!/usr/bin/env bash
nohup bin/hive --service metastore &
nohup bin/hive --service hiveserver2 &
jps |grep Run|grep -v jps |cut -d' ' -f 1|xargs kill -9