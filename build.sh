#!/bin/bash

sbt clean assembly
docker build --tag=anadkatmeet/eosio-data-scrapper:latest .
docker push anadkatmeet/eosio-data-scrapper:latest