package com.example.flore.myapplication;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

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
    int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
            RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format

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
        double maxVal, freq, binNo = 0;

        short sData[] = new short[BufferElements2Rec];

        DoubleFFT_1D fft1d = new DoubleFFT_1D(BufferElements2Rec);

        double[] fftBuffer = new double[BufferElements2Rec * 2];

        while (isRecording) {
            //recording Audio data into sData
            recorder.read(sData, 0, BufferElements2Rec);
            /*
            for (int i = 0; i < BufferElements2Rec - 1; ++i) {
                fftBuffer[2 * i] = sData[i];
                fftBuffer[2 * i + 1] = 0;
            }
            */
            //perform a hamming window on the signal in order to filter noise
            //  double[] a = new double[(BufferElements2Rec + 24 * BufferElements2Rec) * 2];

            System.arraycopy(applyWindow(sData), 0, fftBuffer, 0, BufferElements2Rec);
            //fft on audio
            fft1d.realForward(fftBuffer);

            /* find the peak magnitude and it's index */
            double maxMag = Double.NEGATIVE_INFINITY;
            int maxInd = -1;

            for (int i = 0; i < fftBuffer.length / 2; ++i) {
                double re = fftBuffer[2 * i];
                double im = fftBuffer[2 * i + 1];
                double mag = Math.sqrt(re * re + im * im);

                if (mag > maxMag) {
                    maxMag = mag;
                    maxInd = i;
                }
            }

              /* calculate the frequency */
            double frequency = ((double) RECORDER_SAMPLERATE * maxInd / (BufferElements2Rec/ 2)/2);
            Log.i("freq", String.valueOf(frequency) + "Hz");
            /*
            double[] magnitude = new double[BufferElements2Rec / 2];
            maxVal = 0;
            for (int i = 0; i < (BufferElements2Rec / 2) - 1; ++i) {

                double real = fftBuffer[2 * i];
                double imaginary = fftBuffer[2 * i + 1];

                magnitude[i] = Math.sqrt(real * real + imaginary * imaginary);
                Log.i("mag", String.valueOf(magnitude[i]) + " " + i);

                //find peak magnitude
                for (int j = 0; j < (BufferElements2Rec / 2) - 1; ++j) {
                    if (magnitude[j] > maxVal) {
                        maxVal = (int) magnitude[j];
                        binNo = j;
                    }
                }

                //results
                freq = 8000 * binNo / (BufferElements2Rec / 2);
                freq = freq / 2;
                Log.i("freq", "Bin " + String.valueOf(binNo));
                Log.i("freq", String.valueOf(freq) + "Hz");

            }
        }
        */
        }
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
}
/*
    private void writeAudioDataToFile() {

        //get the path to sdcard
        File pathToExternalStorage = Environment.getExternalStorageDirectory();
        //to this path add a new directory path and create new App dir (InstroList) in /documents Dir
        File appDirectory = new File(pathToExternalStorage.getAbsolutePath() + "/teste/teste");
        // have the object build the directory structure, if needed.
        appDirectory.mkdirs();
        String fileName = "8k16bitMono.pcm";
        File filePath = new File(appDirectory, fileName);
        // Write the output audio in byte
        //  String filePath = "/sdcard/8k16bitMono.pcm";

        short sData[] = new short[BufferElements2Rec];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            Log.e("ERRR", "Could not create file", e);
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format
            recorder.read(sData, 0, BufferElements2Rec);
            System.out.println("Short wirting to file" + sData.toString());
            try {
                // writes the data to file from buffer stores the voice buffer
                byte bData[] = short2byte(sData);

                os.write(bData, 0, BufferElements2Rec * BytesPerElement);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

