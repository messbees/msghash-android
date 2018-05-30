package com.gotowork.msghash;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.List;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;
import net.hockeyapp.android.CrashManager;

public class MainActivity extends AppCompatActivity {
    private static final String FIRST = "first";
    private static final String NORMAL = "normal";
    private static final String UPDATE = "update";
    public EditText editText, editName;
    public Button buttonSend;

    List<Message> messages;
    ListView listView;
    MessageAdapter messageAdapter;
    public static MainActivity context;
    private SharedPreferences sharedPreferences;
    private KeyPair keyPair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SugarContext.init(this);
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);

        load();
        initializeComponents();
    }

    private void initializeComponents() {
        editText = (EditText) findViewById(R.id.editText);
        editName = (EditText) findViewById(R.id.editName);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(messageAdapter);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message(editName.getText().toString(), editText.getText().toString());
                messages.add(message);
                message.save();
                messageAdapter.update();

                editName.setText("");
                editText.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(keyPair);
        editor.putString("key_pair", json);
        editor.apply();
    }

    private void load() {
        if (checkFirstRun().equals(FIRST)) {
            makeToast("First launch");
            if (generateKeys()) {
                save();
                Message message = new Message("messbees", "Welcome!");
                message.save();

                try {
                    messages = Message.listAll(Message.class);
                }
                catch (Exception e) {

                }
            } else {
                forceStop();
            }
        } else {
            makeToast("not first launch");
            messages = Message.listAll(Message.class);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("key_pair", "");
            try {
                keyPair = gson.fromJson(json, KeyPair.class);
            } catch (Exception e) {
                makeToast("Failed to load: " + e.getLocalizedMessage() + "\n" + "Generating new keys...");
                if (!generateKeys()) {
                    forceStop();
                }
            }
        }
        messageAdapter = new MessageAdapter(this, messages, keyPair);
        messageAdapter.update();
    }

    private boolean generateKeys() {
        try {
            keyPair = Sawtooth.getKeyPair();
            return true;
        } catch (NoSuchAlgorithmException e) {
            makeToast(e.getMessage());
        } catch (NoSuchProviderException e) {
            makeToast(e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            makeToast(e.getMessage());
        }
        return false;
    }

    private void makeToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void forceStop() {
        finish();
        System.exit(0);
    }

    private String checkFirstRun() {

        final String PREFS_NAME = "PrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (savedVersionCode == DOESNT_EXIST) {
            prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
            return FIRST;
        }
        else {
            if(currentVersionCode > savedVersionCode) {
                prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
                return UPDATE;
            }
            else if (currentVersionCode > savedVersionCode){
                return "WTF";
            }
            else {
                prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
                return NORMAL;
            }
        }

    }
}
