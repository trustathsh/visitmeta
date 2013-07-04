#!/bin/sh

if [ -z $IFMAPCLI ]; then
    echo "set IFMAPCLI environment with 'export IFMAPCLI=/path/to/ifmapcli/jars'"
    exit 1
fi

COMMAND="java -jar $IFMAPCLI"

################################################################################

ACCESS_REQUEST=111:33
MAC_ADDRESS=ee:ee:ee:ee:ee:ee

echo "delete access-request-mac from $ACCESS_REQUEST and $MAC_ADDRESS"
$COMMAND/ar-mac.jar delete $ACCESS_REQUEST $MAC_ADDRESS > /dev/null
