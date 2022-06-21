package com.example.webrtcdelayestimator;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
public class Constants {

    public static final int SAMPLE_RATE = 48000;
    public static final int ROOM_PORT = 8888;
    public static int TCP_Port = 4002;
    public static boolean RUN_THREAD = true;
    public static String FILEPATH;


    public static int JAVARECORDER_FRAME_SIZE = 0;


    public static int minBufferSize;
    public static int TCP_COM_PORT = 4003;
    //    100, 106, 110, 111, 113, 119, 135,
//            139, 143, 144, 179, 199, 254, 255, 280, 311, 389, 427, 443, 445, 464, 465, 497, 513, 514, 515, 543, 544, 548, 554, 587, 593, 625, 631, 636,
//            646, 787, 808, 873, 902, 990, 993, 995,
    public static int WELL_KNOWN_PORTS[] = {1000, 1022, 1024, 1033, 1035, 1036, 1037, 1038, 1039, 1040, 1041, 1044, 1048, 1049, 1050, 1053, 1054,
            1056, 1058, 1059, 1064, 1065, 1066, 1069, 1071, 1074, 1080, 1110, 1234, 1433, 1494, 1521, 1720, 1723, 1755, 1761, 1801, 1900, 1935, 1998, 2000,
            2003, 2005, 2049, 2103, 2105, 2107, 2121, 2161, 2301, 2383, 2401, 2601, 2717, 2869, 2967, 3000, 3001, 3128, 3268, 3306, 3389, 3689, 3690, 3703,
            3986, 4000, 4001, 4045, 4899, 5000, 5001, 5003, 5009, 5050, 5051, 5060, 5101, 5120, 5190, 5357, 5432, 5555, 5631, 5666, 5800, 5900, 5901, 6000,
            6001, 6002, 6004, 6112, 6646, 6666, 7000, 7070, 7937, 7938, 8000, 8002, 8008, 8009, 8010, 8031, 8080, 8081, 8443, 8888, 9000, 9001, 9090, 9100,
            9102, 9999, 10001, 10010, 32768, 32771, 49152, 49157, 50000};


    public static Activity activity = null;

    public static void INITIALIZE() {
        JAVARECORDER_FRAME_SIZE = (40 * SAMPLE_RATE) / 1000; // Require to define buffer size
        minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);

//        if (framestomilli(minBufferSize / 2, SampleRate) == frameTimeInMS) // NOT PSTN
//        {
////            FrameSize_10MS = minBufferSize / 2;
//        } else if (frameTimeInMS == 10) {
//            minBufferSize = 960;
////            FrameSize_10MS = 960 / 2;
//        }
//
//
//        FrameTimeStamp = -FrameSize_10MS;

    }

}
