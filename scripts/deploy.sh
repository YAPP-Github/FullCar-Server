source ~/.bashrc
$(aws ecr get-login --region ap-northeast-2 --no-include-email)
sudo docker rm -f codedeploy
sudo docker rmi "$ECR_REPOSITORY"
sudo docker pull "$ECR_REPOSITORY"
sudo docker run -d --name codedeploy -p 8080:80 "$ECR_REPOSITORY"