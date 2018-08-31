The EOS data challenge
----------------------

1. Run the `nodeos` binary with the `--deep-mind` flag
2. Dig each transaction (on standard output)
3. Identify all the `eosio::newaccount` (`action`s  with `account: eosio` and `name: newaccount`)
4. Output data about `creator` and the created account `name` to some place you can query.
5. Query that data to identify all accounts created by the `eosio` account.

* You only need to process a few thousand blocks, no need to sync the whole chain. 
* Killing the process during initial syncing sometimes corrupts data in `nodeos-data`, simply wipe it out when this happens.
* You can replay the history faster with `--replay-blockchain`.  Otherwise, clean out the `nodeos-data` and restart `nodeos`.

How to run this
---------------

On a machine with a decent unix shell (linux, osx, windows+git-bash, ...) and Docker installed,
Launch the following command:

./boot.sh

Clean up the nodeos-data folder (watch out, the owner will be "root" because of the Docker uid mapping)
