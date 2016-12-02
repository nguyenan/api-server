#!/bin/bash
usage ()
{
  echo 'Usage : Script <install|start|stop|restart|status>'
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
		if [ ! -f newrelic-infra.yml ]; then
			echo '"newrelic-infra.yml" required'
			exit
		fi
		sudo bash -c "cat newrelic-infra.yml > /etc/newrelic-infra.yml"
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
			echo "> sudo initctl $ACTION newrelic-infra"
			sudo initctl $ACTION newrelic-infra
			sudo initctl status newrelic-infra
			;;
			'16.04'|'16.10')
			echo "> sudo systemctl $ACTION newrelic-infra"
			sudo systemctl $ACTION newrelic-infra
			sudo systemctl status newrelic-infra
			;;
		esac

	;;
	*)
		usage
	;;
esac
