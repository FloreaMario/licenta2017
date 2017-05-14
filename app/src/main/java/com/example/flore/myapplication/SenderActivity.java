package com.example.flore.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SenderActivity extends AppCompatActivity {
    Sender mySender = new Sender();
    Vocabulary myVocab = new Vocabulary();
    EditText txtFreq, txtTime;
    private boolean isEmitting = false;

    String frequency;

    int time;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);
        setButtonHandlers();
    }
    private void enableButtons(boolean isEmitting) {
        enableButton(R.id.btnTransmit, !isEmitting);
        enableButton(R.id.btnStopTransm, isEmitting);
    }
    private void enableButton(int id, boolean isEnable) {
        ((Button) findViewById(id)).setEnabled(isEnable);
    }

    private void setButtonHandlers() {
        ((Button) findViewById(R.id.btnTransmit)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.btnStopTransm)).setOnClickListener(btnClick);
    }

    public View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            final EditText txtFreq = (EditText)findViewById(R.id.txtFreqV);
            frequency = txtFreq.getText().toString();
         //   final EditText txtTime = (EditText)findViewById(R.id.txtTime);
            switch (v.getId()) {

                case R.id.btnTransmit: {
                    enableButtons(true);
                    isEmitting = true;
                    char[] inputText = frequency.toCharArray();
                    myVocab.frequencyConvert(inputText);
                    break;
                }
                case R.id.btnStopTransm: {
                    mySender.stopEmission();
                    isEmitting = false;
                    enableButtons(false);

                    break;
                    }
                }

            }

    };

}