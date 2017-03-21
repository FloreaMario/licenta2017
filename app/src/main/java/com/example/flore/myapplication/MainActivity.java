package com.example.flore.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtonHandlers();
        enableButtons();
    }

    private void setButtonHandlers() {
        ((Button) findViewById(R.id.btnConSender)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.btnConReceiver)).setOnClickListener(btnClick);

    }

    private void enableButton(int id, boolean isEnable) {
        ((Button) findViewById(id)).setEnabled(isEnable);
    }

    private void enableButtons() {
        enableButton(R.id.btnConSender, true);
        enableButton(R.id.btnConReceiver, true);

    }


    private View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btnConReceiver: {
                    enableButtons();
                    Intent intent = new Intent(MainActivity.this, ReceiverActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.btnConSender: {
                    enableButtons();
                    Intent intent = new Intent(MainActivity.this, SenderActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    };
}


