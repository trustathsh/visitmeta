#!/bin/sh

if [ -z $IFMAPCLI ]; then
    echo "set IFMAPCLI environment with 'export IFMAPCLI=/path/to/ifmapcli/jars'"
    exit 1
fi

COMMAND="java -jar $IFMAPCLI"

################################################################################

ACCESS_REQUEST=111:33
MAC_ADDRESS=ee:ee:ee:ee:ee:ee

echo "publish $ACCESS_REQUEST and $MAC_ADDRESS with access-request-mac metadata"
$COMMAND/ar-mac.jar update $ACCESS_REQUEST $MAC_ADDRESS > /dev/null
