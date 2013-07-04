#!/bin/sh

if [ -z $IFMAPCLI ]; then
    echo "set IFMAPCLI environment with 'export IFMAPCLI=/path/to/ifmapcli/jars'"
    exit 1
fi

COMMAND="java -jar $IFMAPCLI"

################################################################################

ACCESS_REQUEST=111:33
DEVICE=device1

echo "delete device-attribute and access-request-device from $ACCESS_REQUEST and $DEVICE"
$COMMAND/dev-attr.jar delete $ACCESS_REQUEST $DEVICE "attribute42" > /dev/null
$COMMAND/ar-dev.jar delete $ACCESS_REQUEST $DEVICE > /dev/null
