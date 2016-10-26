#!/bin/bash

echo "Killing Currently Running Server"

docker stop api
docker rm api

echo "Done stoping server"
