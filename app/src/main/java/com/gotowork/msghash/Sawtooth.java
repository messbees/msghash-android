package com.gotowork.msghash;

import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import sawtooth.sdk.protobuf.Transaction;
import sawtooth.sdk.protobuf.TransactionHeader;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;

public class Sawtooth {
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "SC");
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
        keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    private static byte[] encodePayload(String verb, String hash) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new CborEncoder(byteArrayOutputStream).encode(new CborBuilder().addMap().put("Verb", verb).put("Message", hash).end().build());
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        return byteArrayOutputStream.toByteArray();
    }

    private static String getAddress(String string) {
        String prefix = Hashing.getHash("msghash");
        String address = Hashing.getHash(string);
        return prefix + address;
    }

    private static TransactionHeader getTransactionHeader(String verb, String message) {
        TransactionHeader.Builder builder = TransactionHeader.newBuilder();
        String key = MainActivity.getPublicHex();
        builder.setBatcherPublicKey(key); //TODO: add batcher public key
        builder.setFamilyName(MainActivity.context.getString(R.string.family_name));
        builder.setFamilyVersion(MainActivity.context.getString(R.string.family_version));
        builder.setInputs(0, getAddress(message));
        builder.setOutputs(0, getAddress(message));
        int nonce = new Random(Calendar.getInstance().getTimeInMillis()).nextInt(1000000000);
        builder.setNonce(String.valueOf(nonce));
        byte[] payload = encodePayload(verb, message);
        String hashedPayload = "";
        try {
            hashedPayload = Hashing.getHash(payload);
        }
        catch (NoSuchAlgorithmException e) {

        }
        builder.setPayloadSha512(hashedPayload);
        builder.setSignerPublicKey(key);
        return builder.build();
    }

    private static void sendTransaction(String publicKey) {

    }

    public static void pin(String hashedMessage) {

    }

    public static void check(String hashedMessage) {

    }
}
//TODO: correct keys string