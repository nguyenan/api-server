#!/bin/bash

echo "Running Server"
echo "Starting server at $(date)"
nohup ant run-server >> server.log 2>&1&
echo "Done starting server"
