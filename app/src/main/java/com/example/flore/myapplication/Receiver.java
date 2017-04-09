package com.example.flore.myapplication;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import org.jtransforms.fft.DoubleFFT_1D;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by flore on 3/21/2017.
 */

public class Receiver {
    //Variables
    private static final int RECORDER_SAMPLERATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    private double[] window;
    double var = 0;
    double prevVar = 0;
    int j = 0;
    int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
            RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format
    double[] fftBuffer = new double[BufferElements2Rec * 2];
    double[] assid = new double[BufferElements2Rec];
    //Methods
    public void startRecording() {

        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);

        recorder.startRecording();
        isRecording = true;

        recordingThread = new Thread(new Runnable() {

            public void run() {

                performFFTonRecording();
                //writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }


    public void stopRecording() {
        // stops the recording activity
        if (null != recorder) {
            isRecording = false;


            recorder.stop();
            recorder.release();

            recorder = null;
            recordingThread = null;
        }
    }

    private void performFFTonRecording() {
        /**
         * Function that performs the fast fourier transform on the audio sample
         */
        short sData[] = new short[BufferElements2Rec];

        DoubleFFT_1D fft1d = new DoubleFFT_1D(BufferElements2Rec);
        j = 0;


        while (isRecording) {
            //recording Audio data into sData
            recorder.read(sData, 0, BufferElements2Rec);

            System.arraycopy(applyWindow(sData), 0, fftBuffer, 0, BufferElements2Rec);
            //fft on audio
            fft1d.realForward(fftBuffer);

            // the ui element will be updated with the assid
            // getAssid();
            }
        }

    /**
     * parse the fft buffer and check for existent frequencies
     * push the frequencies into an array. This array will represent the ASSISD received
     * @return
     */
    private double[] getAssid(void)
    {
        //parse the fft buffer and check for existent frequencies
        for (int i = 0; i < fftBuffer.length / 2; ++i)
        {
            double varFFT = fftBuffer[i];
            //if frequency is greater than a prefedined threshold
            if(varFFT > 60000)
            {
                var = getFreqfromInd(i);
                //if frequency is different from the previous stored frequency(with a threshold)
                if(((var >= prevVar +50) || (var <= prevVar -50)) && (var!=0))
                {
                    //push the frequencies into an array. This array will represent the ASSISD received
                    assid[j] = var / 2;
                    prevVar = var;
                    j++;
                }
            }
            else
            {
                //do nothing
            }
        }
        return assid;
    }

    /**
     *  Function that returns the magnitude of the FFT buffer
     * @return
     */
    private double returnMagnitude(void)
    {
       /* find the peak magnitude and it's index */
       double maxMag = Double.NEGATIVE_INFINITY;
       int maxInd = -1;
       //calculate magnitude
       for (int i = 0; i < fftBuffer.length / 2; ++i) {
           double re = fftBuffer[2 * i];
           double im = fftBuffer[2 * i + 1];
           double mag = Math.sqrt(re * re + im * im);

           if (mag > maxMag) {
               maxMag = mag;
               maxInd = i;
           }

       }
       return maxInd;
                /* calculate the frequency */
        //double frequency = getFreqfromInd(maxInd);
        //  Log.i("freq", String.valueOf(frequency) + "Hz");
   }

    /**
     * Function that receives the ind and computes the frequency of it
     * @param maxInd
     * @return frequency
     */
    private double getFreqfromInd(int maxInd)
    {
        double frequency = ((double) RECORDER_SAMPLERATE * maxInd / (BufferElements2Rec/ 2)/2);
        return frequency;
    }

    private byte[] short2byte ( short[] sData){
            int shortArrsize = sData.length;
            byte[] bytes = new byte[shortArrsize * 2];

            for (int i = 0; i < shortArrsize; i++) {
                bytes[i * 2] = (byte) (sData[i] & 0x00FF);
                bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
                sData[i] = 0;
            }
            return bytes;
        }

    public static byte[] toByteArray ( double[] doubleArray){
            int times = Double.SIZE / Byte.SIZE;
            byte[] bytes = new byte[doubleArray.length * times];
            for (int i = 0; i < doubleArray.length; i++) {
                ByteBuffer.wrap(bytes, i * times, times).putDouble(doubleArray[i]);
            }
            return bytes;
        }

    private void buildHammWindow(int size) {
        if (window != null && window.length == size) {
            return;
        }
        window = new double[size];
        for (int i = 0; i < size; ++i) {
            window[i] = .54 - .46 * Math.cos(2 * Math.PI * i / (size - 1.0));
        }
    }

    /**
     * apply a Hamming window filter to raw input data
     *
     * @param input an array containing unfiltered input data
     * @return a double array containing the filtered data
     */
    private double[] applyWindow(short[] input) {
        double[] res = new double[input.length];

        buildHammWindow(input.length);
        for (int i = 0; i < input.length; ++i) {
            res[i] = (double) input[i] * window[i];
        }
        return res;
    }

    public boolean verifyFreq(double frequency)
        {
        /**
         * Function receives as parameter a desired frequency. IT will check to see if
         * the frequency is present in the audio environment, and reflect on the UI the changes
         */
        int maxInd = 0;
        double fftValue;
        double FFTTHRESHOLD = 50000;
        double firstVal;
        boolean returnVal;

        //double frequency = ((double) RECORDER_SAMPLERATE * maxInd / (BufferElements2Rec/ 2)/2);

        // get from the frequency the indicator where we could find it
        firstVal = (2 * frequency * (BufferElements2Rec/ 2))/RECORDER_SAMPLERATE;
        maxInd = (int)(firstVal + 0.5d);

        fftValue = fftBuffer[maxInd * 2];
        if(fftValue > FFTTHRESHOLD)
        {
            returnVal = true;
        }
        else
        {
            returnVal = false;
        }
        return returnVal;
    }
}
