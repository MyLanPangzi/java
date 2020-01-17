#ConfigMap

##创建方式
###目录 

```shell script
kubectl create cm config --from-file=.
kubectl get cm
kubectl describe cm
kubectl delete cm config
kubectl edit cm config
```

###文件

```shell script
kubectl create cm env --from-file=./env #k是文件名，v是内容
kubectl create cm env2 --from-env-file=./env #kv原样导入
kubectl get cm
kubectl describe cm
kubectl delete cm env
kubectl edit cm env

```

###kv

```shell script
kubectl create cm env --from-literal=a=b --from-literal=c=d
```

###yml

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: config
data:
  k: v
```
##使用

###环境变量

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: busybox
spec:
  containers:
    - name: busybox
      image: busybox
      command:
        - sh
        - -c
        - env
      env:
        - name: LOG_LEVEL
          valueFrom:
            configMapKeyRef:
              name: env
              key: LOG_LEVEL
      envFrom:
        - configMapRef:
            name: config
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: env
data:
  LOG_LEVEL: INFO
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: config
data:
  hello: world
```

###卷挂载

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: config-volume
spec:
  containers:
    - name: busybox
      image: busybox
      command: ["sh","-c","cat /etc/configmap/hello.txt && sleep 3600;"]
      volumeMounts:
        - mountPath: /etc/configmap
          name: config-volume
  volumes:
    - name: config-volume
      configMap:
        name: config-volume
        items:
          - key: hello
            path: hello.txt
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: config-volume
data:
  hello: world
```