#!/bin/sh
export R_HOME=/Library/Frameworks/R.framework/Resources
export CLASSPATH=.:/Library/Frameworks/R.framework/Resources/library/rJava/jri
export LD_LIBRARY_PATH=/Library/Frameworks/R.framework/Resources/library/rJava/jri/
java -Djava.library.path="$LD_LIBRARY_PATH" -jar dataservice.jar
