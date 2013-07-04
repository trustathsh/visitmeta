#!/bin/sh

if [ -z $IFMAPCLI ]; then
    echo "set IFMAPCLI environment with 'export IFMAPCLI=/path/to/ifmapcli/jars'"
    exit 1
fi

COMMAND="java -jar $IFMAPCLI"

################################################################################

ACCESS_REQUEST=111:33
DEVICE=device1

echo "publish $ACCESS_REQUEST and $DEVICE with device-attribute and access-request-device metadata"
$COMMAND/dev-attr.jar update $ACCESS_REQUEST $DEVICE "attribute42" > /dev/null
$COMMAND/ar-dev.jar update $ACCESS_REQUEST $DEVICE > /dev/null
