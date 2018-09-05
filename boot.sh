#!/bin/bash

COMMAND="/opt/eos/bin/nodeos --deep-mind --config-dir /nodeos-config --genesis-json /nodeos-config/genesis.json --data-dir /nodeos-data"

errorexit() {
        echo $1
        exit 1
}

test -d nodeos-data ||  errorexit "You the missing the nodeos-data/ folder. Are you in the right path ?"
test -d nodeos-config ||  errorexit "You are missing the nodeos-config/ folder. Are you in the right path ?"

echo "##########################################################################################"
echo "# Starting the docker container with the following command:"
echo "# $COMMAND"
echo "# Press Ctrl-C to kill the process"
echo "##########################################################################################"
sleep 2

docker run -ti --rm \
      -p 9876:9876 \
      -p 8888:8888 \
      -v ${PWD}/nodeos-data:/nodeos-data \
      -v ${PWD}/nodeos-config:/nodeos-config \
      -w /opt/eos/bin \
      --hostname eosdatachallenge \
      --name eosdatachallenge \
      gcr.io/eoscanada-public/eosio-nodeos-full:v1.2.1 \
      $COMMAND