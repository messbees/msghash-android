package com.gotowork.msghash;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        Preference copyPublic = findPreference("copy_public");
        copyPublic.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //TODO copy public key
                return false;
            }
        });

        Preference copyPrivate = findPreference("copy_private");
        copyPrivate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //TODO copy private key
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
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });
    }
}
