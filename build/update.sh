#!/bin/bash

echo "Updating Server...."
./stop.sh
git pull
ant clean
./run.sh

