# MsgHash v0.0.3
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
- Fix wrong message hash
- Fix wrong getAddress()

## Next Versions
-------------
v0.0.3: Fully assemble TransactionHeader + sign it with ECDSA spec256k1 key;
v0.1: Pin message's hash to blockchain and check whether it is already pinned, also new design
