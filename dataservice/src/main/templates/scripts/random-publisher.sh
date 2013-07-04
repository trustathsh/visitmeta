#!/bin/bash

if [ -z $IFMAPCLI ]; then
    echo "set IFMAPCLI environment with 'export IFMAPCLI=/path/to/ifmapcli/jars'"
    exit 1
fi

COMMAND="java -jar $IFMAPCLI"

################################################################################

if [ $# -ne 1 ]; then
    echo "usage: $(basename $0) COUNT"
    exit 1
fi


ACCESS_REQUEST="111:33"

echo "publishing data for $USER ..."
for i in `seq 1 $1`; do
    user="user-"$RANDOM
    ip=$(($RANDOM % 255 + 1))"."$(($RANDOM % 255 + 1))"."$(($RANDOM % 255 + 1))"."$(($RANDOM % 255 + 1))
    mac="de:ad:be:ef:"$(($RANDOM % 9 + 1))$(($RANDOM % 9 + 1))":"$(($RANDOM % 9 + 1))$(($RANDOM % 9 + 1))

    echo -e $ip"\t"$mac
    $COMMAND/ar-ip.jar update $ACCESS_REQUEST $ip > /dev/null
    $COMMAND/pdp.jar update $user $ip $mac > /dev/null

done
