#!/bin/sh

if [ -z $IFMAPCLI ]; then
    echo "set IFMAPCLI environment with 'export IFMAPCLI=/path/to/ifmapcli/jars'"
    exit 1
fi

COMMAND="java -jar $IFMAPCLI"

################################################################################

$COMMAND/purge.jar me > /dev/null

export IFMAP_USER=mapclient
export IFMAP_PASS=mapclient

$COMMAND/purge.jar me > /dev/null

export IFMAP_USER=dhcp
export IFMAP_PASS=dhcp

$COMMAND/purge.jar me > /dev/null
