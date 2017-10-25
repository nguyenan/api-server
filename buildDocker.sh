#!/bin/sh
python build.py -c > /tmp/build.txt
cat /tmp/build.txt | grep -i error
read -p "Build docker image (y/n)?" CONT
if [ "$CONT" = "y" ]; then
	python build.py -i
	python build.py -b
	VERSION=$(date +%d%b)
	docker tag tendsell/api7:latest tendsell/api7:$VERSION
	docker tag tendsell/api7:latest tendsell/api8:$VERSION
	docker push tendsell/api7:$VERSION
	docker push tendsell/api8:$VERSION
	docker images | grep tendsell
	read -p "Start tendsell/api8:$VERSION local (y/n)?" CONT2
	if [ "$CONT2" = "y" ]; then
		docker run --rm -d -p 808:8080 -p 443:8443 -i -t tendsell/api8:$VERSION
		docker ps
	fi
fi
