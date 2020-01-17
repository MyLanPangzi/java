#Secrets
##创建

```shell script
echo -n "admin"|base64
echo -n "123456"|base64
```

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: secret
  labels:
    test: secret
data:
  username: YWRtaW4=
  password: MTIzNDU2
---
apiVersion: v1
kind: Pod
metadata:
  name: secret-pod
  labels:
    test: secret
spec:
  containers:
    - name: busybox
      image: busybox
      command: ["sh","-c"," echo $uname && env && sleep 3600"]
      env:
        - name: uname
          valueFrom:
            secretKeyRef:
              name: secret
              key: username
      envFrom:
        - secretRef:
            name: secret
      volumeMounts:
        - mountPath: /etc/secret
          name: secret
  volumes:
    - name: secret
      secret:
        secretName: secret
```


##使用