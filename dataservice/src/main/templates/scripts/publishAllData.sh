#!/bin/sh

TIMEOUT=5

echo "### Run: 01-single-identifier-with-metadata.sh ###"
sh ./01-single-identifier-with-metadata.sh
echo "### Done, sleep $TIMEOUT seconds. ###"
sleep $TIMEOUT
echo "### Run: 02-second-graph-with-multiple-identifiers.sh ###"
sh ./02-second-graph-with-multiple-identifiers.sh
echo "### Done, sleep $TIMEOUT seconds. ###"
sleep $TIMEOUT
echo "### Run: 03-connect-graph-01-and-02.sh ###"
sh ./03-connect-graph-01-and-02.sh
echo "### Done, sleep $TIMEOUT seconds. ###"
sleep $TIMEOUT
echo "### Run: 04-new-mulitValue-metadata-on-identifier.sh ###"
sh ./04-new-mulitValue-metadata-on-identifier.sh
echo "### Done, sleep $TIMEOUT seconds. ###"
sleep $TIMEOUT
echo "### Run: 05-new-multiValue-metadata-on-link.sh ###"
sh ./05-new-multiValue-metadata-on-link.sh
echo "### Done, sleep $TIMEOUT seconds. ###"
sleep $TIMEOUT
echo "### Run: 06-delete-metadata-from-05.sh ###"
sh ./06-delete-metadata-from-05.sh
echo "### Done, sleep $TIMEOUT seconds. ###"
sleep $TIMEOUT
echo "### Run: 07-delete-metadata-from-04.sh ###"
sh ./07-delete-metadata-from-04.sh
echo "### Done, sleep $TIMEOUT seconds. ###"
sleep $TIMEOUT
echo "### Run: 08-delete-sub-graph-from-02.sh ###"
sh ./08-delete-sub-graph-from-02.sh
echo "### Done, sleep $TIMEOUT seconds. ###"
sleep $TIMEOUT
echo "### Run: 09-delete-single-identifier.sh ###"
sh ./09-delete-single-identifier.sh
echo "### Done, sleep $TIMEOUT seconds. ###"
sleep $TIMEOUT
echo "### Run: 10-new-singleValue-metadata-on-link.sh ###"
sh ./10-new-singleValue-metadata-on-link.sh
echo "### Done, sleep $TIMEOUT seconds. ###"
sleep $TIMEOUT
echo "### Run: 11-delete-metadata-from-10.sh ###"
sh ./11-delete-metadata-from-10.sh
echo "### Done, sleep $TIMEOUT seconds. ###"
sleep $TIMEOUT
echo "### Run: 12-metadata-from-different-publisher.sh ###"
sh ./12-metadata-from-different-publisher.sh
echo "### Done. ###"

exit 0
