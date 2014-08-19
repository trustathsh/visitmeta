#!/bin/sh
export R_HOME=/usr/lib/R
export CLASSPATH=.:/usr/lib/R/site-library/rJava/jri
export LD_LIBRARY_PATH=/usr/lib/R/site-library/rJava/jri/
java -Djava.library.path="$R_HOME/site-library/rJava/jri" -jar dataservice.jar