#!/usr/bin/python

from subprocess import call
import os
import argparse
import subprocess
import sys

parser = argparse.ArgumentParser()
parser.add_argument("-se", "--setup", help="setup a new server", action="store_true")
parser.add_argument("-p", "--project", help="specify only a specific project")
parser.add_argument("-s", "--server", help="specify only a specific server")
parser.add_argument("-cl", "--clean", help="clean all projects", action="store_true")
parser.add_argument("-c", "--compile", help="compile all projects", action="store_true")
parser.add_argument("-i", "--install", help="locally install all projects", action="store_true")
parser.add_argument("-b", "--build", help="build a docker image of the server", action="store_true")
parser.add_argument("-d", "--deploy", help="deploy the docker image to docker hub", action="store_true")
parser.add_argument("-a", "--all", help="clean, compile, install, build, and deploy all projects", action="store_true")
args = parser.parse_args()

servers = ["ad.webutilitykit.com"]

if (args.all is True):
	args.clean = True
	args.compile = True
	args.install = True
	args.build = True
	args.deploy = True

if (args.server is not None and args.server != ""):
	if (len(args.server) == 2):
		servers = [args.server + ".webutilitykit.com"]
	else:	
		servers = [args.server]

# NOTE: core must come first

projects = ["core","email","storage","file","analytics","image","misc","help","search","payment","code","services","dns","cdn","user"]

if (args.project is not None and args.project != ""):
	projects = [args.project]

if (projects.count("core") < 1):
	projects.insert(0, "core")

#parentDirectory = os.getcwd()

webappsLocation = "/Applications/tomcat/webapps"

if (args.setup):
	# copy derby war from apache to server
	for server in servers:
		# copy derby war
		serverWebappsLocation = "root@"+server+":/var/lib/tomcat7/webapps"
		warLocation = "derby.war"
		call(["scp", warLocation, serverWebappsLocation])
		
		# copy SSL keystore	
		call(["scp", "build/api.webutilitykit.com.keystore", "/var/lib/tomcat7"])

		# copy derby library files
		serverLibLocation = "root@"+server+":/usr/share/tomcat7/lib"
		libraries = ["derby","derbyclient","derbynet"]
		for library in libraries:
			libLocation = "build/serverlibs/" + library + ".jar" 
			call(["scp", libLocation, serverLibLocation])

# permissions
# chmod a+x /var/lib/tomcat7/webapps/code/linux/ubuntu64/phantomjs

for project in projects:
	#print "project %s:" % (project)
	pomLocation = project+"/pom.xml"
	warLocation = project+"/target/"+project+".war"
	webappsLocation = "build/webapps"

	status = 0

	if (args.clean):
		print "cleaning %s:" % (project)
		status = call(["mvn", "-f", pomLocation, "clean"])
		
	if (args.compile):
		print "compiling %s:" % (project)
		call(["mvn", "-f", pomLocation, "package"])
		if (project == "core"):
			status = call(["mvn", "-f", pomLocation, "install"])
		
	if (args.install):
		if not os.path.exists(webappsLocation):
    			os.makedirs(webappsLocation)
		print "installing %s:" % (project)
		if (project != "core"):
                        warLocation = project+"/target/"+project+".war"
                        status = call(["cp", warLocation, "build/webapps"])

	if (status != 0):
		sys.exit(status)
	else:
		print "status %s" % (status)

if (args.build):
	print "dockerizing..."
	#for project in projects:
	#	if (project != "core"): 				
	#		print "project %s:" % (project)
	#		warLocation = project+"/target/"+project+".war"

	# docker build
	call(["docker", "build", "-t", "tendsell/api7", "build"])	
       	
	print "removing war files..." 
	call(["rm", "-rf", "build/webapps/*"])

	# sudo docker ps -l -q
	# docker commit `dl` helloworld
	# docker commit `docker ps -l -q` rpalmite/webutilitykit

if (args.deploy):
	print "deploying docker container to dockerhub..."
	containerId = subprocess.Popen(["docker", "images", "-q", "tendsell/api7"], stdout=subprocess.PIPE).communicate()[0].rstrip()
	#//containerId = subprocess.Popen(["docker", "ps", "-l", "-q"], stdout=subprocess.PIPE).communicate()[0]
	call(["echo", "running " + containerId + "..."])
        call(["docker", "run", "-d", "-p", "80:8080", "-p", "443:8443", "--name", "api", containerId])
	call(["docker", "commit", "api", "tendsell/api7"])		
        call(["docker", "push", "tendsell/api7"])		
	
	# clean up # todo -- this can go after commit before push
	call(["docker", "kill", "api"])
	call(["docker", "rm", "api"])

#	for server in servers:
#		serverAddr = "root@"+server;
		#serverWebappsLocation = serverAddr+":/var/lib/tomcat7/webapps"
		#print "deploying to server %s:" % (server)

		#for project in projects:
		#	if (project != "core"):				
		#		print "project %s:" % (project)
		#		warLocation = project+"/target/"+project+".war"
		#		call(["scp", warLocation, serverWebappsLocation])	

		#call(["bash", "setup.sh", server])


print "finished."


