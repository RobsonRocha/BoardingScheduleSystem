#!/bin/bash

if [[ "$(docker images -q ogc-boardingschedule:local 2> /dev/null)" == "" ]]; then
  docker build -t ogc-boardingschedule:local .;
fi;

docker run --rm -it -p 8080:8080 ogc-boardingschedule:local;