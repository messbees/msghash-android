# MsgHash v0.0.2
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
- Fix bug that caused crash on start
- Add more funcs to Sawtooth.java (WIP)

## Next Versions
-------------
v0.0.3: Fully assemble TransactionHeader + sign it with ECDSA spec256k1 key;
v0.1: Pin message's hash to blockchain and check whether it is already pinned, also new design
