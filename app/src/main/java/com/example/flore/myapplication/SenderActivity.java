package com.example.flore.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.flore.myapplication.R.id.time;

public class SenderActivity extends AppCompatActivity {
    Sender mySender = new Sender();
    EditText txtFreq, txtTime;
    Button btnEmit;
    double frequency;
    int time;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);
        setButtonHandlers();
    }

    private void setButtonHandlers() {
        ((Button) findViewById(R.id.btnEmit)).setOnClickListener(btnClick);
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            final EditText txtFreq = (EditText)findViewById(R.id.txtFreq);
            final EditText txtTime = (EditText)findViewById(R.id.txtTime);
            switch (v.getId()) {

                case R.id.btnEmit: {
                    frequency = Double.parseDouble(txtFreq.getText().toString());
                    time = Integer.parseInt(txtTime.getText().toString());
                    mySender.playSound(frequency, time);
                    break;
                }

            }
        }
    };
}