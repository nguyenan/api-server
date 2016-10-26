

sudo nohup java -Dhttp.port=80 -Dhttps.port=443 -Dkeystore.name=wut.keystore -Dkeystore.password=REPLACE_ME -Dkeymanager.password=REPLACE_ME -jar wut.jar &

sudo nohup ant run-server &


git update

git clone https://rpalmite@bitbucket.org/rpalmite/frontend.git applications
Password: REPLACE_ME
