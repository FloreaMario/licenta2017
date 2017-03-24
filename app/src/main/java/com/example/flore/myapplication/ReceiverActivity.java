package com.example.flore.myapplication;

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
        ((Button)findViewById(R.id.btnStart)).setOnClickListener(btnClick);
        ((Button)findViewById(R.id.btnStop)).setOnClickListener(btnClick);
        ((Button)findViewById(R.id.btnVerifyFreq)).setOnClickListener(btnClick);
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
            final EditText txtFreqV = (EditText)findViewById(R.id.txtFreqV);
            TextView txtResult = (TextView)findViewById(R.id.txtResult);


            boolean verifStatus ;
            switch (v.getId()) {

                case R.id.btnStart: {
                    enableButtons(true);
                    myReceiver.startRecording();
                    break;
                }
                case R.id.btnStop: {
                    enableButtons(false);
                    myReceiver.stopRecording();
                    break;
                }
                case R.id.btnVerifyFreq: {
                    
                    frequencyV = Double.parseDouble(txtFreqV.getText().toString());
                    enableButtons(true);
                    verifStatus = myReceiver.verifyFreq(frequencyV);
                    if(verifStatus == true)
                    {
                        txtResult.setText("Frequency was detected");
                    }
                    else
                    {
                        txtResult.setText("Frequency was NOT detected");
                    }

                    break;
                }
            }
        }
    };
}
