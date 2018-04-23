package com.gotowork.msghash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public EditText editText, editName;
    public Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();

    }

    private void initializeComponents() {
        editText = (EditText) findViewById(R.id.editText);
        editName = (EditText) findViewById(R.id.editName);
        buttonSend = (Button) findViewById(R.id.buttonSend);
    }
}
