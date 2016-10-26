#!/bin/bash          

OPTION=$1
SRCD="/home/"
TGTD="/var/backups/"
# TODO make this take a source file and not a src directory for log files
# other times src dir is ok
if [ "$OPTION" = "log" ]; then
	SRC=server.log	
else
	TGTD=backups
fi
echo "Backing up $OPTION..."
OF=$OPTION-$(date +%Y%m%d).tgz
tar -cZf $TGTD$OF $SRCD
echo "Done backing up $OPTION"
