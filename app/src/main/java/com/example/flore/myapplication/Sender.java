package com.example.flore.myapplication;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by flore on 3/21/2017.
 */

public class Sender {

    private Thread transmittingThread = null;
     volatile boolean isEmitting = false;
    public void playSound(double frequency, int duration) {
        // AudioTrack definition
        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM);

        // Sine wave
        double[] mSound = new double[duration];
        short[] mBuffer = new short[duration];
        for (int i = 0; i < mSound.length; i++) {
            mSound[i] = Math.sin((2.0*Math.PI * i/(44100/frequency)));
            mBuffer[i] = (short) (mSound[i]*Short.MAX_VALUE);
        }

        mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
        mAudioTrack.play();

        mAudioTrack.write(mBuffer, 0, mSound.length);
        mAudioTrack.stop();
        mAudioTrack.release();

    }
    public void stopEmission()
    {
        isEmitting = false;
        transmittingThread = null;
    }

    public void startEmit(final int[] inputFreq)
    {
        isEmitting = true;
        transmittingThread = new Thread(new Runnable() {

            public void run() {


                    while(isEmitting == true) {
                        //play SOF
                        playSound(17000, 11000);
                        for(int i=0; i< inputFreq.length; i++)
                        {
                            if(inputFreq[i]!= 0)
                            {
                                playSound(inputFreq[i], 11000);
                            }

                        }

                    }

            }
        }, "Transmitting Thread ");
        transmittingThread.start();

    }
}
