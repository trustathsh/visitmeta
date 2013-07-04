#!/bin/sh

TIMEOUT=3

ACCESS_REQUEST="111:33"
DEVICE_55="111:55"
DEVICE_3="111:3"
DEVICE_0="111:0"
MAC="11:22:33:44:55:66"
IP_32="10.99.99.32"
IP_7="192.168.0.7"
IP_5="10.0.0.5"
IP_6="10.0.0.6"
IDENTITY="john.smith"

if [ $# -ne 1 ]; then
    echo "usage: $(basename $0) PATH-TO-IFMAPCLI-JARS"
    exit 1
fi

IFMAPCLI_PATH=$1
COMMAND="java -jar $IFMAPCLI_PATH"

$COMMAND/ar-dev.jar update $ACCESS_REQUEST $DEVICE_55
$COMMAND/dev-attr.jar update $ACCESS_REQUEST $DEVICE_55 "av-signature-out-of-date"
sleep $TIMEOUT

$COMMAND/ar-mac.jar update $ACCESS_REQUEST $MAC
sleep $TIMEOUT

$COMMAND/ip-mac.jar update $IP_32 $MAC
$COMMAND/ip-mac.jar update $IP_7 $MAC
sleep $TIMEOUT

$COMMAND/auth-as.jar update $ACCESS_REQUEST $IDENTITY
sleep $TIMEOUT

$COMMAND/role.jar update $ACCESS_REQUEST $IDENTITY "finance and employee"
sleep $TIMEOUT

$COMMAND/layer2-info.jar update $ACCESS_REQUEST $DEVICE_3 978 vlan 12
$COMMAND/dev-ip.jar update $DEVICE_3 $IP_5
$COMMAND/dev-ip.jar update $DEVICE_0 $IP_6
sleep $TIMEOUT


$COMMAND/auth-by.jar update $ACCESS_REQUEST $DEVICE_0


# TODO periodically identifier add/remove
# TODO periodically metadata add/remove

echo "done."
