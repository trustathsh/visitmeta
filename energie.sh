#!/bin/bas
VERSION=0.3.0
mvn clean install
cd visitmeta-distribution
cd target
unzip visitmeta-distribution-$VERSION-bundle.zip
cd ..
cd ..
