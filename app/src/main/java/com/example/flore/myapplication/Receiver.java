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
import java.util.ArrayList;
import java.util.stream.IntStream;

import static java.lang.Math.sqrt;

/**
 * Created by flore on 3/21/2017.
 */

public class Receiver {
    Vocabulary myVocab = new Vocabulary();

    //Variables
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    private double[] window;
    double currentFreq = 0;
    double prevFreq = 0;
    double prevFreqValue = 17000;
    int j = 0;
    int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
            RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

    int BufferElements2Rec = bufferSize; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format
    int prevVarFFT = 0;

    double[] fftBuffer = new double[BufferElements2Rec * 2];
    double[] assid = new double[100];
    double[] real = new double[BufferElements2Rec];
    double[] imag = new double[BufferElements2Rec];
    double[] mag = new double[BufferElements2Rec];
    double[] assid1 = new double[100];
    double[] assid2 = new double[100];
    double[] assid3 = new double[100];

    boolean commBit = false;//SOF and EOF bit
    boolean commFrame = true;

    /****************DEFINES*************/
    int MINIMBIN = 1300;
    int SOFFREQ = 17000;
    int THRESHOLDFREQ = 100;
    int MAXFREQ = 700;
    int counter = 0;

    //Methods
    public void startRecording() {

        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, bufferSize * 2);

        recorder.startRecording();
        isRecording = true;
        assid = new double[100];
        recordingThread = new Thread(new Runnable() {

            public void run() {

                performFFTonRecording();
                //writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }


    public double[] stopRecording() {
        // stops the recording activity

        char[] stringTable1= new char[100];
        char[] stringTable2= new char[100];
        char[] stringTable3= new char[100];
        commBit = false;
        counter = 0;
        if (null != recorder) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            recordingThread = null;
        }

        //assid = performAvg(assid1, assid2, assid3);
        char[] myCharFinal = new char[100];
        myCharFinal = myVocab.masterConvert(assid1,assid2,assid3);

        assid1 = new double[100];
        assid2 = new double[100];
        assid3 = new double[100];
        return assid;

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
            mag = new double[BufferElements2Rec];
            for(int i = 0 ; i < BufferElements2Rec; i++)
            {
                real[i] = fftBuffer[2*i];
                imag[i] = 0;
                mag[i] = sqrt(real[i]*real[i]+imag[i]*imag[i]);
            }

            // the ui element will be updated with the assid

            //parse the fft buffer and check for existent frequencies
            getAssid();
        }
    }

    /**
     * parse the fft buffer and check for existent frequencies
     * push the frequencies into an array. This array will represent the ASSISD received
     * @return
     */
    private void getAssid()
    {


        //parse the fft buffer and check for existent frequencies
        for (int i = 0; i < mag.length; ++i)
        {
            double varMagnitude = mag[i];
            //if frequency is greater than a prefedined threshold
            if((i>MINIMBIN) && (i < MINIMBIN + 297))
            {
                MAXFREQ = 500;
            }
            else
            {
                MAXFREQ = 300;
            }
            if((varMagnitude > MAXFREQ)&&(i>MINIMBIN))
            {
                //get frequency value
                currentFreq = getFreqfromInd(i);
                //17000 hz acts as Start of frame
                if((commFrame == true) &&
                        (currentFreq >= SOFFREQ - THRESHOLDFREQ) && (currentFreq <= SOFFREQ + THRESHOLDFREQ))
                {
                    commBit = true;

                    commFrame = false;
                    switch (counter) {
                        case 1:
                            assid1 = assid;
                            break;
                        case 2:
                            assid2 = assid;
                            break;
                        case 3:
                            assid3 = assid;
                            break;
                        default:
                            counter = 0;
                            break;
                    }
                    counter++;
                    assid = new double[100];
                    j = 0;
                    prevFreqValue = currentFreq;
                }

                //if frequency is different from the previous stored frequency(with a threshold)
                //and commBit is set to true
                if((commBit == true) && (currentFreq!=0) && (currentFreq >= (SOFFREQ+THRESHOLDFREQ))
                        &&((currentFreq >= prevFreq + THRESHOLDFREQ) || (currentFreq <= prevFreq - THRESHOLDFREQ)))
                {
                    commFrame = true;
                    //push the frequencies into an array. This array will represent the ASSISD received
                    assid[j] = currentFreq;
                    prevFreq = currentFreq;

                    j++;
                }

            }
            else
            {
                //do nothing
            }
        }
    }
    public double[] performAvg(double firstValue[], double secondValue[], double thirdValue[])
    {

        double[] returnValue = new double[100];
            int sum1 = 0;
            int sum2 = 0;
            int sum3 = 0;
            for(double i : firstValue)
              sum1 +=i;
            for(double i : secondValue)
               sum2 +=i;
            for(double i : thirdValue)
              sum3 +=i;
            if((sum1> sum2) &&(sum1> sum3))
            {
                returnValue = firstValue;
            }
            if((sum2 > sum1) &&(sum2> sum3))
            {
                returnValue = secondValue;
            }
            if((sum3 > sum1) &&(sum3 > sum2))
            {
                returnValue = thirdValue;
            }
            return returnValue;
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

}
