#!/usr/bin/env bash
kubectl delete svc,pvc,deploy -l app.kubernetes.io/part-of=wordpress
