#!/bin/bash

echo "Starting server at $(date)"

docker run -d -p 80:8080 -p 443:8443 --restart always --name api rpalmite/webutilitykit:latest

echo "Done starting server"

