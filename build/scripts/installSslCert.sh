
480 505 8852

### SS ###

keytool -import -alias root -keystore wut.keystore -trustcacerts -file gd-class2-root.crt

#keytool -import -alias cross -keystore wut.keystore -trustcacerts -file gd_cross_intermediate.crt

keytool -import -alias intermed -keystore wut.keystore -trustcacerts -file gd_intermediate.crt

keytool -import -alias wut -keystore wut.keystore -trustcacerts -file secretsaviors.com.crt




### STRIPE ###

keytool -import -alias stripe -keystore 12.keystore -trustcacerts -file stripe.cer

### INSTALLATION FOR RANCHO SONORA ###

keytool -import -trustcacerts -alias AddTrustExternalCARoot -file AddTrustExternalCARoot.crt -keystore www.ranchosonora.com.keystore

keytool -import -trustcacerts -alias PositiveSSLCA2 -file PositiveSSLCA2.crt -keystore www.ranchosonora.com.keystore

keytool -import -trustcacerts -alias www.ranchosonora.com -file www_ranchosonora_com.crt -keystore www.ranchosonora.com.keystore

