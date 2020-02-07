mkdir -p configure-pod-container/configmap/
wget https://kubernetes.io/examples/configmap/game.properties -O configure-pod-container/configmap/game.properties
wget https://kubernetes.io/examples/configmap/ui.properties -O configure-pod-container/configmap/ui.properties
wget https://kubernetes.io/examples/configmap/game-env-file.properties -O configure-pod-container/configmap/game-env-file.properties
wget https://kubernetes.io/examples/configmap/ui-env-file.properties -O configure-pod-container/configmap/ui-env-file.properties

kubectl create configmap game-config --from-file=configure-pod-container/configmap/
kubectl create configmap game-config-2 --from-file=configure-pod-container/configmap/ui.properties --from-file=configure-pod-container/configmap/game.properties
kubectl create configmap game-env-file --from-env-file=configure-pod-container/configmap/game-env-file.properties
kubectl create configmap config-multi-env-files --from-env-file=configure-pod-container/configmap/game-env-file.properties --from-env-file=configure-pod-container/configmap/ui-env-file.properties
kubectl create configmap game-config-3 --from-file=game-special-key=configure-pod-container/configmap/game.properties
kubectl create configmap special-config --from-literal=special.how=very --from-literal=special.type=charm
cat << EOF >./kustomization.yaml
configMapGenerator:
  - name: game-config-4
    files:
      - configure-pod-container/configmap/game.properties
EOF

cat <<EOF >./kustomization.yaml
configMapGenerator:
  - name: game-config-5
    files:
      - game-special-key=configure-pod-container/configmap/game.properties
EOF