package com.gotowork.msghash;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends PreferenceFragment {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);


        findPreference("copy_public").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences sharedPreferences = MainActivity.context.getPreferences(MODE_PRIVATE);
                String text = sharedPreferences.getString("public", "");
                copy(text);
                return false;
            }
        });

        findPreference("copy_private").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences sharedPreferences = MainActivity.context.getPreferences(MODE_PRIVATE);
                String text = sharedPreferences.getString("private", "");
                copy(text);
                return false;
            }
        });

        findPreference("copy_public_hex").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences sharedPreferences = MainActivity.context.getPreferences(MODE_PRIVATE);
                String text = sharedPreferences.getString("public_hex", "");
                copy(text);
                return false;
            }
        });

        findPreference("copy_private_hex").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences sharedPreferences = MainActivity.context.getPreferences(MODE_PRIVATE);
                String text = sharedPreferences.getString("private_hex", "");
                copy(text);
                return false;
            }
        });

        findPreference("check_hash").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.hash));
                builder.setMessage(Hashing.getHash("sample"));
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNeutralButton(R.string.copy, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        copy(Hashing.getHash("sample"));
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });

        findPreference("copy_encoded_payload").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                byte[] bytes = Sawtooth.encodePayload("verb", "message");
                String string = new String(bytes, StandardCharsets.UTF_8);
                copy(string);
                return false;
            }
        });

        findPreference("copy_hashed_payload").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                byte[] bytes = Sawtooth.encodePayload("verb", "message");
                String hash = "";
                try {
                    hash = Hashing.getHash(bytes);
                }
                catch (NoSuchAlgorithmException e) {
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                copy(hash);
                return false;
            }
        });

        findPreference("copy_hexed_payload").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                byte[] bytes = Sawtooth.encodePayload("verb", "message");
                String string = new String(bytes, StandardCharsets.UTF_8);
                String hash = Hashing.getHash(string);
                String hex = String.format("%040x", new BigInteger(1, hash.getBytes(StandardCharsets.UTF_8)));
                copy(hex);
                return false;
            }
        });

        findPreference("copy_address").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                copy(Sawtooth.getAddress("hello"));
                return false;
            }
        });
    }

    public void copy(String string) {
        ClipboardManager clipboard = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", string);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), R.string.copied, Toast.LENGTH_LONG).show();
    }
}
