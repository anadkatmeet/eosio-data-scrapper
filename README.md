The EOS data challenge
----------------------

There are 3 docker containers involved. 1st runs the EOSIO blockchain, 2nd runs mongodb database and 3rd runs data-scrapper app.

Blockchain container will produce files as output under eosio-output/raw

Data-scrapper image will read raw files, will fetch newaccounts, push to **mongodb database(eosio)**, in **collection(newaccount)** and move raw files to eosio-output/processed folder


How to run this
---------------

On a machine with a decent unix shell (linux, osx, windows+git-bash, ...) and Docker installed,
Launch the following commands:

Clean up the nodeos-data folder (watch out, the owner will be "root" because of the Docker uid mapping)

####1. Run with pre-built images 

Step 1: ./boot.sh | split -d --lines 100 - eosio-output/raw/dump-

Step 2: docker-compose up

####2. Run by building custom data-scrapper image

Step 1: sbt assembly (Note: you will need Scala Build Tool to run this) 

Step 2: ./boot.sh | split -d --lines 100 - eosio-output/raw/dump-

Step 3: docker-compose -f docker-compose-custom.yml up --build

