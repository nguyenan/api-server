#!/bin/bash
projects=( "core" "email" "storage" "file" "payment" "code" "dns" "cdn" "user" )
dockerimage="tendsell/api7"
version=$(date -u +"%Y%m%d_%H-%M-%S")
for project in "${projects[@]}"
do
	pomLocation=$project"/pom.xml"
	warLocation=$project"/target/"$project".war"
	webappsLocation="build/webapps"

#compile
	echo "compiling $project"
	mvn -f $pomLocation package
	if (project == "core"); then
		mvn -f $pomLocation install
	fi
#install
	echo "installing $project"
	mvn -f $pomLocation package
	if (project != "core"); then
		warLocation = $project+"/target/"+$project+".war"
		cp $warLocation build/webapps
	fi
done

read -p "Build docker image (y/n)?" CONT
if [ "$CONT" = "y" ]; then
#build
	echo "dockerizing..."
	# docker build
	docker build -t $dockerimage:$version build

	echo "removing war files..." 
	rm -rf build/webapps/*
fi

read -p "Push docker image (y/n)?" CONT
if [ "$CONT" = "y" ]; then
#build
	echo "dockerizing..."
	# docker build
	docker build -t $dockerimage:$version build
fi
