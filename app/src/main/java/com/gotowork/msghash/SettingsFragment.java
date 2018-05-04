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
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import sawtooth.sdk.protobuf.TransactionHeader;

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

        findPreference("custom_message").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.custom_message));
                final View view = (LinearLayout) getActivity().getLayoutInflater()
                        .inflate(R.layout.dialog_custom_message, null);
                builder.setView(view);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = ((EditText) view.findViewById(R.id.customMsgName)).getText().toString();
                        String text = ((EditText) view.findViewById(R.id.customMsgText)).getText().toString();
                        String time = ((EditText) view.findViewById(R.id.customMsgTime)).getText().toString();
                        String fullTime = ((EditText) view.findViewById(R.id.customMsgDate)).getText().toString();
                        Message message = new Message(name, text, time, fullTime);
                        message.save();
                        Toast.makeText(getActivity(), R.string.custom_message_notification, Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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

        findPreference("copy_serialized_header").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                TransactionHeader transactionHeader = Sawtooth.getTransactionHeader("verb", "message");
                copy(transactionHeader.toString());
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
