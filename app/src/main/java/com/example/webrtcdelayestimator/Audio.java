package com.example.webrtcdelayestimator;


import static com.example.webrtcdelayestimator.Constants.minBufferSize;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.Arrays;

public class Audio {

    private static final String TAG = "Audio";

    public int SAMPLE_RATE = 48000;

    float RecordAudioMS = 10;
//    float RecordAudioMS = 40f;

    private AudioRecord audioRecord;
    private AudioTrack audioTrack;
    private int minBufferSizeInBytes;
    private int minBufferSizeInShort;
    private byte[] byteData;
    private short[] shortData;

    public void inititalize() {
        if (RecordAudioMS == 10f) {
            minBufferSizeInBytes = 960 * 2  ;
        } else if (RecordAudioMS == 21.33f) {
            minBufferSizeInBytes = 1024 * 2  ;
        } else if (RecordAudioMS == 40f) {
            minBufferSizeInBytes = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
//            minBufferSizeInBytes = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
            Log.e(TAG, "MIN BUFFER SIZE: " + minBufferSizeInBytes);
            Log.e(TAG, "Entered into 40MS block");
        } else if (RecordAudioMS == 5f) {
            minBufferSizeInBytes = 480 * 2;
        }
        minBufferSizeInShort = minBufferSizeInBytes / 2;
        shortData = new short[minBufferSizeInShort];

        audioRecord = new AudioRecord.Builder()
                .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
                        .build())
                .setBufferSizeInBytes(minBufferSizeInBytes)
                .build();

        audioTrack = new AudioTrack(new AudioAttributes.Builder()
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
                , new AudioFormat.Builder()
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .setSampleRate(SAMPLE_RATE)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .build()
                , minBufferSizeInBytes, AudioTrack.MODE_STREAM, 0);

    }

    public void startRecording() {
        Log.e(TAG, "AUDIO RECORD: " + minBufferSizeInBytes);
        Log.e(TAG, "MIN BUFFER SIZE: minBufferSize " + minBufferSize + " minBufferSizeInShort " + minBufferSizeInShort);
//        audioRecord = new AudioRecord(MIC_MODE, SampleRate, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);



        audioRecord.startRecording();
    }

    public void startPlaying(){
        audioTrack.play();
    }

    long previous = 0;

    public byte[] getRecordedData() {
        long start = System.nanoTime() / 1000000;
        byte[] byteData = new byte[minBufferSizeInBytes];
        int readData = audioRecord.read(byteData, 0, minBufferSizeInBytes);
        Log.e(TAG, "AUDIO RECORD: READ DATA: " + readData + " and time: " + (start - previous));
        previous = start;
        return byteData;
    }

    public short[] getRecordedDataShort() {
        long start = System.nanoTime() / 1000000;
        int readData = audioRecord.read(shortData, 0, minBufferSizeInShort);
        Log.d(TAG, "AUDIO RECORD: READ DATA: " + readData + " and time: " + (start - previous));
        previous = start;

        //for stero
        short[] shortDataStero = new short[minBufferSizeInShort / 2];
        int j = 0;
        for (int i = 1; i < shortData.length; i = i + 2) {
            shortDataStero[j] = shortData[i];
            j++;
        }

        Log.d(TAG, "Stero Data: " + shortDataStero.length);
        return shortDataStero;
    }

    public void stopRecording() {
        try {

            audioRecord.stop();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR: " + e.toString());

        }
        try {

            audioRecord.release();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR: " + e.toString());

        }

    }


    public void stopPlaying(){
        try {

            audioTrack.stop();    // Close AudioRecord

        } catch (Exception e) {

            e.printStackTrace();
            Log.e(TAG, "ERROR: " + e.toString());

        }
        try {

            audioTrack.release();

        } catch (Exception e) {

            e.printStackTrace();
            Log.e(TAG, "ERROR: " + e.toString());

        }
    }

    public void play(byte[] byteData){
        audioTrack.write(byteData,0,byteData.length);
    }

    public void play(float[] floatData){
        audioTrack.write(floatData,0,byteData.length, AudioTrack.MODE_STREAM);
    }

    public void play(short[] shortData){
        Log.d(TAG, "PLAYING DATA:"+ Arrays.toString(shortData));
        audioTrack.write(shortData,0,shortData.length);
    }
}

