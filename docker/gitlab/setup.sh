tee gitlab.rb <<EOF
external_url 'https://192.168.11.136/'
gitlab_rails['initial_root_password'] = File.read('/run/secrets/gitlab_root_password')
EOF
tee root_password.txt <<EOF
Xiebo0409
EOF
tee docker-compose.yml <<EOF
version: "3.6"
services:
  gitlab:
    image: gitlab/gitlab-ce:latest
    ports:
      - "22:22"
      - "80:80"
      - "443:443"
    volumes:
      - gitlab-data:/var/opt/gitlab
      - gitlab-logs:/var/log/gitlab
      - gitlab-config:/etc/gitlab
    environment:
      GITLAB_OMNIBUS_CONFIG: "from_file('/omnibus_config.rb')"
    configs:
      - source: gitlab
        target: /omnibus_config.rb
    secrets:
      - gitlab_root_password
  gitlab-runner:
    image: gitlab/gitlab-runner:alpine
    deploy:
      mode: replicated
      replicas: 1
volumes:
  gitlab-data:
  gitlab-logs:
  gitlab-config:
configs:
  gitlab:
    file: ./gitlab.rb
secrets:
  gitlab_root_password:
    file: ./root_password.txt
EOF
