#!/bin/sh

find / -type f -size +50000k -exec ls -lh {} \; | awk '{ print $8 ": " $5 }'

# or

du -a /var | sort -n -r | head -n 10


# OR

ls -lS | head +10



#!/bin/sh

DISC=$1
PARTITION=`df -h|grep $DISC|awk Ô{print $1}Õ`
SIZE =`df -h| grep $DISC|awk Ô{print $2}Õ`
USED =`df -h|grep $DISC|awk Ô{print $3}Õ`
FREE =`df -h|grep $DISC|awk Ô{print $4}Õ`

echo ÒPartition: $PARTITIONÓ
echo ÒTotal size:$SIZEÓ
echo ÒUsed space:$USEDÓ
echo ÒFree space:$FREEÓ

# OR

fdisk -l

df -h

