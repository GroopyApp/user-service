docker build --tag user-service:latest --platform=linux/amd64 .
docker tag user-service:latest aledanna/user-service
docker push aledanna/user-service