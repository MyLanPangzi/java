kubectl create secret docker-registry aliyun --docker-server=https://registry.cn-hangzhou.aliyuncs.com \
--docker-username=twocat0409 --docker-password=Xiebo0409 --docker-email=smailXie@163.com
kubectl patch serviceaccount default -p '{"imagePullSecrets": [{"name": "myregistrykey"}]}'

