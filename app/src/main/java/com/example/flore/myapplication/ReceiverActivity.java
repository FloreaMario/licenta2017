package com.example.flore.myapplication;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ReceiverActivity extends AppCompatActivity {

    Receiver myReceiver = new Receiver();
    double frequencyV;char[] assidText;
    char[] oldAssid;
    String print = " ";
    private Thread receiverThread = null;
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver2);
        setButtonHandlers();
        enableButtons(false);
    }

    private void setButtonHandlers() {
        ((Button) findViewById(R.id.btnStart)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.btnStop)).setOnClickListener(btnClick);

    }

    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnStart, !isRecording);
        enableButton(R.id.btnStop, isRecording);
    }

    private void enableButton(int id, boolean isEnable) {
        ((Button) findViewById(id)).setEnabled(isEnable);
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {

            TextView txtAssid = (TextView) findViewById(R.id.txtASSID);

            double[] assid;
            String print = "";
            switch (v.getId()) {

                case R.id.btnStart: {
                    enableButtons(true);
                    myReceiver.startRecording();
                    isRecording = true;
                    print =  startReceive();
                    txtAssid.setText(print);

                    break;
                }
                case R.id.btnStop: {
                    isRecording = false;
                    enableButtons(false);
                    assid = myReceiver.stopRecording();
                    print = "";
                    txtAssid.setText(print);

                    break;
                }

            }
        }
    };
    private String startReceive()
    {
        receiverThread = new Thread(new Runnable() {

            public void run() {
                while(isRecording == true) {

                    assidText = myReceiver.getASSIDtext();
                    if (assidText != oldAssid) {
                        for (int i = 0; i < assidText.length; i++) {

                            if (assidText[i] != 0) {
                                print += assidText[i];
                                print += " ";
                            }
                        }

                        oldAssid = assidText;


                    }
                }
            }
        }, "Transmitting Thread ");
        receiverThread.start();
        return print;
    }

}
