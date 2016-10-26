#!/bin/bash

if [ $1 == "create" ]; then
    echo "Enter client domain name (ie www.client.com) [ENTER]:"
	read domain
	
	echo "Enter client business name (ie Client Services Inc) [ENTER]:"
	read name
	
	echo "Enter client city (ie San Jose) [ENTER]:"
	read city
	
	echo "Enter client state (ie California) [ENTER]:"
	read state
	
	echo "Enter client 2 letter country code (ie US) [ENTER]:"
	read country
	
	echo "Domain information will be cn=$domain, ou=IT, o=$name, l=$city, st=$state, c=$country"
	
	keytool -keysize 2048 -keyalg RSA -genkey -alias "$domain" -dname "cn=$domain, ou=IT, o=$name, l=$city, st=$state, c=$country" -keystore ${domain}.keystore
	
	keytool -certreq -keyalg RSA -alias "$domain" -file "${domain}.csr" -keystore "${domain}.keystore"
	
	cat "${domain}.csr"
	
elif [ $1 == "install" ]; then
	echo "SSL Provider (ie positivessl) [ENTER]:"
	read provider
	
	echo "Enter client domain name (ie www.client.com) [ENTER]:"
	read domain
	
	if [ $provider == "positivessl" ]; then
		keytool -import -trustcacerts -alias AddTrustExternalCARoot -file AddTrustExternalCARoot.crt -keystore "${domain}.keystore"
	
		keytool -import -trustcacerts -alias PositiveSSLCA2 -file PositiveSSLCA2.crt -keystore "${domain}.keystore"
		
		cp `echo ${domain} | sed -e 's/\./_/g'`.crt ${domain}.crt
		
		keytool -import -trustcacerts -alias ${domain} -file "${domain}.crt" -keystore "${domain}.keystore"
	fi
else
  echo "Usage: ssl.sh [create | install]"
fi



