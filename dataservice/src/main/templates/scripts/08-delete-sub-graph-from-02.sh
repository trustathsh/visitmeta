#!/bin/sh

if [ -z $IFMAPCLI ]; then
    echo "set IFMAPCLI environment with 'export IFMAPCLI=/path/to/ifmapcli/jars'"
    exit 1
fi

COMMAND="java -jar $IFMAPCLI"

################################################################################

IP_ADDRESS=10.0.0.1
MAC_ADDRESS=ee:ee:ee:ee:ee:ee
USERNAME=joe

echo "delete pdp subgraph"
$COMMAND/pdp.jar delete $USERNAME $IP_ADDRESS $MAC_ADDRESS > /dev/null
