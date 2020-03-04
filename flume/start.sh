#!/usr/bin/env bash
NC_PORT=44444 bin/flume-ng agent --conf conf --conf-file job/example.conf --name a1 -Dflume.root.logger=INFO,console -DpropertiesImplementation=org.apache.flume.node.EnvVarResolverProperties
bin/flume-ng agent –-conf conf -z zkhost:2181,zkhost1:2181 -p /flume –n a1 -Dflume.root.logger=INFO,console