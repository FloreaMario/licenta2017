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
    double frequencyV;

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
                    print = "";
                    break;
                }
                case R.id.btnStop: {
                    enableButtons(false);
                    assid = myReceiver.stopRecording();
                    int[] assidInt = new int[assid.length];
                    for (int i = 0; i < assid.length; i++) {

                        assidInt[i] = (int) (assid[i] + 0.5d);
                        if (assidInt[i] != 0) {
                            print += assidInt[i];
                            print += " ";
                        }
                    }
                    txtAssid.setText(print);
                    break;
                }

            }
        }
    };
    
}
