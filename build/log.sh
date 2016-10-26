#!/bin/bash
LINES=$1
if [ "$LINES" = "" ]; then
	LINES=100
fi
echo "Viewing server log"
docker logs --tail=$LINES api
