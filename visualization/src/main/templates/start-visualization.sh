#!/bin/sh
cd `dirname $0`
JAVA_ARGS=
if [ `uname -s` = "Darwin" ] ; then
    JAVA_ARGS="$JAVA_ARGS -Dapple.laf.useScreenMenuBar=true"
    JAVA_ARGS="$JAVA_ARGS -Xdock:icon=./img/visitmeta-icon-128px.png"
    JAVA_ARGS="$JAVA_ARGS -Xdock:name=VisITMeta"
fi
exec java $JAVA_ARGS -jar visitmeta-visualization.jar
