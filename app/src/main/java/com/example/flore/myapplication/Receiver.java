package com.example.flore.myapplication;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import org.jtransforms.fft.DoubleFFT_1D;

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
            recorder.read(sData, 0, BufferElements2Rec);


            for (int i = 0; i < BufferElements2Rec - 1; ++i) {
                fftBuffer[2 * i] = sData[i];
                fftBuffer[2 * i + 1] = 0;
            }

            fft1d.complexForward(fftBuffer);

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
    }
}

/*
    private void writeAudioDataToFile() {

        //get the path to sdcard
        File pathToExternalStorage = Environment.getExternalStorageDirectory();
        //to this path add a new directory path and create new App dir (InstroList) in /documents Dir
        File appDirectory = new File(pathToExternalStorage.getAbsolutePath()  + "/teste/teste");
        // have the object build the directory structure, if needed.
        appDirectory.mkdirs();
        String fileName = "8k16bitMono.pcm";
        File filePath = new File (appDirectory, fileName);
        // Write the output audio in byte
      //  String filePath = "/sdcard/8k16bitMono.pcm";

        short sData[] = new short[BufferElements2Rec];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            Log.e("ERRR", "Could not create file",e);
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
    }
*/
