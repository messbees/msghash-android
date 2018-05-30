package com.gotowork.msghash;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.protobuf.ByteString;

import org.bitcoinj.core.ECKey;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.util.Calendar;
import java.util.Random;

import co.nstant.in.cbor.CborException;
import sawtooth.sdk.protobuf.BatchList;
import sawtooth.sdk.protobuf.BatchHeader;
import sawtooth.sdk.protobuf.Transaction;
import sawtooth.sdk.protobuf.TransactionHeader;
import sawtooth.sdk.protobuf.Batch;
import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import sawtooth.sdk.protobuf.TransactionList;

import static android.content.Context.MODE_PRIVATE;

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

    public static byte[] encodePayload(String verb, String hash) throws CborException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new CborEncoder(byteArrayOutputStream).encode(new CborBuilder().addMap().put("Verb", verb).put("Message", hash).end().build());
        return byteArrayOutputStream.toByteArray();
    }

    public static String getAddress(String string) {
        String prefix = Hashing.getHash("msghash").substring(0,6);
        String address = Hashing.getHash(string).substring(64);
        return prefix + address;
    }

    public static TransactionHeader getTransactionHeader(String verb, String hashedMessage) throws CborException {
        TransactionHeader.Builder builder = TransactionHeader.newBuilder();
        String key = "1cf1266e282c41be5e4254d8820772c5518a2c5a8c0c7f7eda19594a7eb539453e1ed7"; //TODO: add batcher public key
        builder.setBatcherPublicKey(key);
        builder.setFamilyName(MainActivity.context.getString(R.string.family_name));
        builder.setFamilyVersion(MainActivity.context.getString(R.string.family_version));
        builder.addInputs(getAddress(hashedMessage));
        builder.addOutputs(getAddress(hashedMessage));
        int nonce = new Random(Calendar.getInstance().getTimeInMillis()).nextInt(1000000000);
        builder.setNonce(String.valueOf(nonce));
        byte[] payload = encodePayload(verb, hashedMessage);
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
    public static Transaction getTransaction(TransactionHeader header, String headerSignature, byte[] payload) {
        Transaction.Builder builder = Transaction.newBuilder();
        builder.setHeader(header.toByteString());

        builder.setHeaderSignature(headerSignature);
        builder.setPayload(ByteString.copyFrom(payload));
        return builder.build();
    }
    public static TransactionList getTransactionList() {
        TransactionList.Builder builder = TransactionList.newBuilder();

        return builder.build();
    }

    public static BatchHeader getBatchHeader(String publicKey, String transactionHeaderSignature) {
        BatchHeader.Builder builder = BatchHeader.newBuilder();
        builder.setSignerPublicKey(publicKey);
        builder.addTransactionIds(transactionHeaderSignature);
        return builder.build();
    }
    public static Batch getBatch(BatchHeader batchHeader, String batchHeaderSignature, Transaction transaction) {
        Batch.Builder builder = Batch.newBuilder();
        builder.setHeader(batchHeader.toByteString());
        builder.setHeaderSignature(batchHeaderSignature);
        builder.addTransactions(transaction);
        return builder.build();
    }
    public static BatchList getBatchList(Batch batch) {
        BatchList.Builder builder = BatchList.newBuilder();
        builder.addBatches(batch);
        return builder.build();
    }

    public static String sign(ECKey privateKey, byte[] bytes) {
        return Signing.sign(privateKey, bytes);
    }

    public static void pin(ECKey keyPair, String hashedMessage) throws CborException, SignatureException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        TransactionHeader transactionHeader = getTransactionHeader("pin", hashedMessage);
        String transactionHeaderSignature = sign(keyPair, transactionHeader.toByteArray());
        byte[] payload = encodePayload("pin", hashedMessage);
        Transaction transaction = getTransaction(transactionHeader, transactionHeaderSignature, payload);
        BatchHeader batchHeader = getBatchHeader(keyPair.getPublicKeyAsHex(), transactionHeaderSignature);
        String batchHeaderSignature = sign(keyPair, batchHeader.toByteArray());
        Batch batch = getBatch(batchHeader, batchHeaderSignature, transaction);
        BatchList batchList = getBatchList(batch);
        byte[] batchListBytes = batchList.toByteArray();

        HTTPParser httpParser = new HTTPParser();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
        String url = sharedPreferences.getString("url", "0.0.0.0");
        int port = sharedPreferences.getInt("port", 1234);
        httpParser.sendRequest(batchListBytes, url, port);
        //return headerSignature;
    }

    public static void check(String hashedMessage) {

    }
}
//TODO: correct keys string