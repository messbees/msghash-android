# MsgHash v0.0.3-debug
-------------

## Download:
-------------
https://rink.hockeyapp.net/manage/apps/758981/app_versions/27/builds/26095591

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
- Fix wrong message hash
- Fix wrong getAddress()

## Next Versions
-------------
v0.0.3: Fully assemble TransactionHeader + sign it with ECDSA spec256k1 key;
v0.1: Pin message's hash to blockchain and check whether it is already pinned, also new design
