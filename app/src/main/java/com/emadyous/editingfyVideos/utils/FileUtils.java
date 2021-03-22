/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;

import com.emadyous.editingfyVideos.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileUtils {

    static class a implements FilenameFilter {
        private final String a;

        a(String str) {
            this.a = str;
        }

        public boolean accept(File file, String str) {
            return str != null && str.startsWith(this.a.substring(0, this.a.lastIndexOf("."))) && str.endsWith(this.a.substring(this.a.lastIndexOf(".")));
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static String getTargetFileName(Context context, String str) {
        String name = new File(str).getAbsoluteFile().getName();
//        String sb = Environment.getExternalStorageDirectory().getAbsoluteFile() +
//                "/" +
//                context.getResources().getString(R.string.MainFolderName) +
//                "/" +
//                "Crop";

        String sb = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                "/" +
                context.getResources().getString(R.string.MainFolderName);

        File absoluteFile = new File(sb).getAbsoluteFile();
        if (!absoluteFile.isDirectory()) {
            absoluteFile.mkdirs();
        }
        List<String> asList = Arrays.asList(Objects.requireNonNull(absoluteFile.list(new a(name))));
        int i = 0;
        while (true) {
            int i2 = i + 1;
            @SuppressLint("DefaultLocale") String sb3 = String.valueOf(name.substring(0, name.lastIndexOf("."))) + "-" +
                    String.format("%d", i) +
                    name.substring(name.lastIndexOf("."));
            if (!asList.contains(sb3)) {
//                String sb4 = Environment.getExternalStorageDirectory().getAbsoluteFile() +
//                        "/" +
//                        context.getResources().getString(R.string.MainFolderName) +
//                        "/" +
//                        "Crop";

                String sb4 = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                        "/" +
                        context.getResources().getString(R.string.MainFolderName);
                return new File(sb4, sb3).getPath();
            }
            i = i2;
        }
    }
}
