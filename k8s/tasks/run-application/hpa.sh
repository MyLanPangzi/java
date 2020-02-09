#!/usr/bin/env bash
while true; do wget -q -O- http://php-apache.default.svc.cluster.local; done;
while true; do wget -q -O- http://php-apache; done;