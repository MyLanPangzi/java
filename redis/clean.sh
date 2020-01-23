#!/usr/bin/bash
kubectl delete svc hello-redis redis
kubectl delete deploy redis hello-redis
