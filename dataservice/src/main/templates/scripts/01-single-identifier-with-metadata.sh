#!/bin/sh

if [ -z $IFMAPCLI ]; then
    echo "set IFMAPCLI environment with 'export IFMAPCLI=/path/to/ifmapcli/jars'"
    exit 1
fi

COMMAND="java -jar $IFMAPCLI"

################################################################################

ACCESS_REQUEST=111:33
CAPABILITY=capability42

echo "publish $ACCESS_REQUEST with capability metadata"
$COMMAND/cap.jar update $ACCESS_REQUEST $CAPABILITY > /dev/null
