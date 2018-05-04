package com.gotowork.msghash;

import android.widget.Toast;

import com.google.protobuf.ByteString;

import org.spongycastle.jce.ECNamedCurveTable;
import org.spongycastle.jce.interfaces.ECPrivateKey;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import sawtooth.sdk.protobuf.Transaction;
import sawtooth.sdk.protobuf.TransactionHeader;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;

public class Sawtooth { //TODO: change all funcs to private (except pin & check)
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "SC");
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
        keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    public static void getPrivateKey(KeyPair keyPair) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        String[] temp = keyPair.getPrivate().toString().split(" S: ");
        String privateKey = temp[1];
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

    public static String getAddress(String string) {
        String prefix = Hashing.getHash("msghash").substring(0,6);
        String address = Hashing.getHash(string).substring(64);
        return prefix + address;
    }

    public static TransactionHeader getTransactionHeader(String verb, String message) {
        TransactionHeader.Builder builder = TransactionHeader.newBuilder();
        String key = "1cf1266e282c41be5e4254d8820772c5518a2c5a8c0c7f7eda19594a7eb539453e1ed7"; //TODO: add batcher public key
        builder.setBatcherPublicKey(key);
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

    public static byte[] getHeaderBytes(TransactionHeader transactionHeader) {
        return transactionHeader.toByteArray();
    }

    public static ByteString getHeaderByteString(TransactionHeader transactionHeader) {
        return transactionHeader.toByteString();
    }

    public static void sendTransaction(String publicKey) {

    }

    public static void pin(String hashedMessage) {

    }

    public static void check(String hashedMessage) {

    }
}
//TODO: correct keys string