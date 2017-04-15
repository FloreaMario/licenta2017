package com.example.flore.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SenderActivity extends AppCompatActivity {
    Sender mySender = new Sender();
    EditText txtFreq, txtTime;
    Button btnEmit;
    private boolean isEmitting = false;
    double frequency;
    int time;
    private Thread transmittingThread = null;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);
        setButtonHandlers();
    }
    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnTransmit, isEmitting);
        enableButton(R.id.btnStopTransm, !isEmitting);
    }
    private void enableButton(int id, boolean isEnable) {
        ((Button) findViewById(id)).setEnabled(isEnable);
    }

    private void setButtonHandlers() {
        ((Button) findViewById(R.id.btnEmit)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.btnTransmit)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.btnStopTransm)).setOnClickListener(btnClick);
    }

    public View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            final EditText txtFreq = (EditText)findViewById(R.id.txtFreqV);
            final EditText txtTime = (EditText)findViewById(R.id.txtTime);
            switch (v.getId()) {

                case R.id.btnEmit: {

                    frequency = Double.parseDouble(txtFreq.getText().toString());
                    time = Integer.parseInt(txtTime.getText().toString());
                    mySender.playSound(frequency, time);
                    break;
                }
                case R.id.btnTransmit: {
                    enableButtons(true);
                    isEmitting = true;
                    startEmit();
                    break;
                }
                case R.id.btnStopTransm: {
                    enableButtons(false);
                    isEmitting = false;
                    transmittingThread = null;
                    break;
                    }
                }

            }

    };
    private void startEmit()
    {
        transmittingThread = new Thread(new Runnable() {

            public void run() {
                while(isEmitting == true) {
                   // mySender.playSound(6969, 44100);
                    mySender.playSound(17000, 11100);//SOF
                    mySender.playSound(18000, 11100);
                    mySender.playSound(18500, 11100);
                    mySender.playSound(19000, 11100);
                    mySender.playSound(19500, 11100);
                    mySender.playSound(20000, 11100);
                    mySender.playSound(20500, 11100);
                    mySender.playSound(21000, 11100);

                }
            }
        }, "Transmitting Thread ");
        transmittingThread.start();

    }
}