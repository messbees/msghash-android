package com.gotowork.msghash;

import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.HashMap;
import java.util.Map;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;

public class Sawtooth {
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "SC");
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
        keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    public static byte[] encodePayload(String verb, String hash) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new CborEncoder(byteArrayOutputStream).encode(new CborBuilder().addMap().put("Verb", verb).put("Message", hash).end().build());
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static void sendTransaction(String hash) {

    }
}
//TODO correct keys string