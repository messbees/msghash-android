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

import java.nio.charset.StandardCharsets;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);


        Preference copyPublic = findPreference("copy_public");
        copyPublic.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences sharedPreferences = MainActivity.context.getPreferences(MODE_PRIVATE);
                String text = sharedPreferences.getString("public", "");
                copy(text);
                return false;
            }
        });

        Preference copyPrivate = findPreference("copy_private");
        copyPrivate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences sharedPreferences = MainActivity.context.getPreferences(MODE_PRIVATE);
                String text = sharedPreferences.getString("private", "");
                copy(text);
                return false;
            }
        });

        Preference checkHash = findPreference("check_hash");
        checkHash.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
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

        Preference copyPayload = findPreference("copy_payload");
        copyPayload.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                byte[] bytes = Sawtooth.encodePayload("verb", "message");
                String string = new String(bytes, StandardCharsets.UTF_8);
                copy(string);
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
