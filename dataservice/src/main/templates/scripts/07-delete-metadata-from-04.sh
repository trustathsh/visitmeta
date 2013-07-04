#!/bin/sh

if [ -z $IFMAPCLI ]; then
    echo "set IFMAPCLI environment with 'export IFMAPCLI=/path/to/ifmapcli/jars'"
    exit 1
fi

COMMAND="java -jar $IFMAPCLI"

################################################################################

ACCESS_REQUEST=111:33
CAPABILITY=capability666

echo "delete capability from $ACCESS_REQUEST"
$COMMAND/cap.jar delete $ACCESS_REQUEST $CAPABILITY > /dev/null
