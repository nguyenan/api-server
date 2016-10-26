#!/usr/bin/python

from subprocess import call
import os
import argparse

parser = argparse.ArgumentParser()
parser.add_argument("-p", "--port", help="port to start service on")
args = parser.parse_args()

if (args.port == None):
	port = "8080"
else:
	port = args.port

call(["docker", "run", "-i", "-p", port+":8080", "-t", "wut"])	

