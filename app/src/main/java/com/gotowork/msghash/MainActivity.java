package com.gotowork.msghash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public EditText editText, editName;
    public Button buttonSend;
    List<Message> messages;
    ListView listView;
    MessageAdapter messageAdapter;
    MainActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

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
}
