package com.example.webrtcdelayestimator;

import static com.example.webrtcdelayestimator.Constants.FILEPATH;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class CommonMethods {

    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        FILEPATH = context.getFilesDir().toString();
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }
        Toast.makeText(context, "No FIle Found", Toast.LENGTH_SHORT).show();
        return null;
    }

    public static byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }
}
