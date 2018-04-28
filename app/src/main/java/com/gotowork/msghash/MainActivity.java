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
import java.security.KeyPair;
import java.security.Security;
import java.util.List;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MainActivity extends AppCompatActivity {

    public EditText editText, editName;
    public Button buttonSend;

    List<Message> messages;
    ListView listView;
    MessageAdapter messageAdapter;

    public static MainActivity context;
    private SharedPreferences sharedPreferences;

    private String keyPublic, keyPrivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
        KeyPair keyPair;
        loadKeys();

        messages = Message.listAll(Message.class);
        messageAdapter = new MessageAdapter(this, messages);
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

    private void saveKeys() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString("private", keyPrivate);
        editor.putString("public", keyPublic);
        editor.apply();
    }

    private void loadKeys() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        keyPrivate = sharedPreferences.getString("private", "");
        keyPublic = sharedPreferences.getString("public", "");
        if (keyPublic == "" || keyPrivate == "") {
            try {
                KeyPair keyPair = Sawtooth.getKeyPair();
                keyPrivate = keyPair.getPrivate().toString();
                keyPublic = keyPair.getPublic().toString();
                saveKeys();
            }
            catch (Exception e) {
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
