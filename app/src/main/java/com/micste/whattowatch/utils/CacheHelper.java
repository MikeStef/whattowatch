package com.micste.whattowatch.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class CacheHelper {

    public static void createGenresFile(Context context, String json) {
        try {
            File file = new File(context.getApplicationInfo().dataDir + "/json_cache/");
            if (!file.exists()) {
                file.mkdir();
            }
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath() + "/" + "genres");
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readGenresJsonFile(Context context) {
        try {
            File file = new File(context.getApplicationInfo().dataDir + "/json_cache/" + "genres");
            if (!file.exists()) {
                return null;
            }
            FileInputStream inputStream = new FileInputStream(file);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
