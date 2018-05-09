# MsgHash v0.0.3-debug
-------------

## Download:
-------------
https://rink.hockeyapp.net/apps/60e9788b4f3d455197689ba0061529e8/app_versions/21

## About
-------------
This version of the app can:
- Post messages and store them in SQLite using SugarORM;
- Generate ECDSA spec256k1 keypair;
- Encode payload (command + message hash);
- Hash it with SHA-512;
- Assemble TransactionHeader (but without batcherPublicKey, WIP)

## Changelog
-------------
- Add custom message creation (Settings -> Add custom message)
- Add full algorithm to assemble transactions and batches
- Add class for HTTP POST request, don't know does it work
- Add test pin() functional
- Rework save/load of keypair

- Fix wrong message hash
- Fix wrong getAddress()

## Next Versions
-------------
v0.0.3: Assembled serialized transaction 
v0.0.4: POST request to REST API
v0.1: Pin message's hash to blockchain and check whether it is already pinned, also new design