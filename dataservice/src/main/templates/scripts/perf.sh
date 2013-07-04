#!/bin/bash

set -e

TMP_LOG_FILE=tmp-log.txt
TMP_OUT_FILE=tmp-out.json
LOGFILE=perf-log.cvs
TIMEOUT=5

i=1
while [ true ]; do
	echo $i > $TMP_LOG_FILE
	./random-publisher.sh 1

    echo "current ..."
	/usr/bin/time -a -o $TMP_LOG_FILE  -f "user=%U,system=%S,elapsed=%e" /usr/bin/wget -O - http://localhost:8000/graph/current > $TMP_OUT_FILE
    timestamp=`cat $TMP_OUT_FILE | python -m json.tool | grep "\"timestamp\"" | tr --delete "\"timestamp:\" "`

    echo "at/$timestamp ..."
	/usr/bin/time -a -o $TMP_LOG_FILE  -f "user=%U,system=%S,elapsed=%e" /usr/bin/wget -O - http://localhost:8000/graph/$timestamp > $TMP_OUT_FILE

    line=`cat $TMP_LOG_FILE | paste -sd ","`
    echo $line >> $LOGFILE
	i=$(($i + 1))
	sleep $TIMEOUT
done
