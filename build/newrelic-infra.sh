#!/bin/bash
usage ()
{
  echo 'Usage : Script <install|start|stop|restart|status> [ -f <license_file>]'
  exit
}

if [ "$#" -lt 1 ]
then
  usage
fi

ACTION=$1
VERSION=$(lsb_release -r -s)
 
case $ACTION in
        'install' )       
		if [ -z $2 ] 
		then
			echo '<license_file> missed'
			usage
		fi
		sudo bash -c "cat $2 > /etc/newrelic-infra.yml"
		curl https://download.newrelic.com/infrastructure_agent/gpg/newrelic-infra.gpg | sudo apt-key add -

		case "$VERSION" in
			'12.04'|'12.10')
			printf "deb http://download.newrelic.com/infrastructure_agent/linux/apt precise main" | sudo tee -a /etc/apt/sources.list.d/newrelic-infra.list
			;;
			'14.04'|'14.10')
			printf "deb http://download.newrelic.com/infrastructure_agent/linux/apt trusty main" | sudo tee -a /etc/apt/sources.list.d/newrelic-infra.list
			;;
			'16.04'|'16.10')
			printf "deb http://download.newrelic.com/infrastructure_agent/linux/apt xenial main" | sudo tee -a /etc/apt/sources.list.d/newrelic-infra.list
			;;
		esac
	
		sudo apt-get updates
		sudo apt-get install newrelic-infra -y
               ;;
        'start'|'stop'|'restart'|'status' )           
               case "$VERSION" in
			'12.04'|'12.10'|'14.04'|'14.10')
			sudo initctl $ACTION newrelic-infra
			;;
			'16.04'|'16.10')
			echo "> sudo systemctl $ACTION newrelic-infra"
			sudo systemctl $ACTION newrelic-infra
			;;
		esac
	;;
	*)
		usage
	;;
esac
