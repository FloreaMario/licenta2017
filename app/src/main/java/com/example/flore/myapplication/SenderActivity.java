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
                    /*
                    mySender.playSound(17000, 5500);//SOF
                    mySender.playSound(18000, 5500);
                    mySender.playSound(18100, 5500);
                    mySender.playSound(18200, 5500);
                    mySender.playSound(18300, 5500);
                    mySender.playSound(18400, 5500);
                    mySender.playSound(18500, 5500);
                    mySender.playSound(18600, 5500);
                    mySender.playSound(18700, 5500);
                    mySender.playSound(18800, 5500);
                    mySender.playSound(18900, 5500);
                    mySender.playSound(19000, 5500);
                    mySender.playSound(19100, 5500);
                    mySender.playSound(19200, 5500);
                    mySender.playSound(19300, 5500);
                    mySender.playSound(19400, 5500);
                    mySender.playSound(19500, 5500);
                    mySender.playSound(19600, 5500);
                    mySender.playSound(19700, 5500);
                    mySender.playSound(19800, 5500);
                    mySender.playSound(19900, 5500);
                    mySender.playSound(20000, 5500);
                    mySender.playSound(20100, 5500);
                    mySender.playSound(20200, 5500);
                    mySender.playSound(20300, 5500);
                    mySender.playSound(20400, 5500);
                    mySender.playSound(20500, 5500);
                    mySender.playSound(20600, 5500);
                    mySender.playSound(20700, 5500);
                    mySender.playSound(20800, 5500);
                    mySender.playSound(20900, 5500);
                    mySender.playSound(21000, 5500);
                    mySender.playSound(21100, 5500);
                    mySender.playSound(21200, 5500);
                    mySender.playSound(21300, 5500);
                    mySender.playSound(21400, 5500);
                    mySender.playSound(21500, 5500);
*/
                    mySender.playSound(17000, 11000);//SOF
                    mySender.playSound(18700, 11000);
                    mySender.playSound(19600, 11000);
                    mySender.playSound(20000, 11000);
                    mySender.playSound(20100, 11000);


                }
            }
        }, "Transmitting Thread ");
        transmittingThread.start();

    }
}