#!/bin/sh
arg1=$1

dir=target
chmod -R 777 target
jar_name=parkingspot-0.0.1-SNAPSHOT.jar

mvn clean install 
chmod -R 777 target

if [ -z "$1" ] ; then
        java -jar $dir/$jar_name
        exit 1

else
	java -jar $dir/$jar_name $arg1

fi
