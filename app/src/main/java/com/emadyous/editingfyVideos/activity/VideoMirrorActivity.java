/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.Statistics;
import com.arthenica.mobileffmpeg.StatisticsCallback;
import com.emadyous.editingfyVideos.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static java.lang.String.format;
import static java.lang.String.valueOf;

public class VideoMirrorActivity extends AppCompatActivity {
    private String videoPath = null;
    static final boolean y = true;

    private VideoView A;
    private String B;

    private String C;
    int a = 4;
    int b = 0;
    boolean c = y;
    TextView o;
    RelativeLayout s;
    RelativeLayout u;
    private FloatingActionButton videoPlayBtn;
    private long executionId;
    int videoLength;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mirror);

        this.c = y;
        copyCreate();
       VideoMirrorActivity.this.c();

    }



    public void c() {
        Intent intent = new Intent(getApplicationContext(), VideoPlayer.class);
        intent.putExtra("song", this.C);
        startActivity(intent);
        finish();
    }

    @SuppressLint("InvalidWakeLockTag")
    public void copyCreate() {
        this.a = e() / 100;
        B = getIntent().getStringExtra("videouri");
        videoPath = B;
        o = (TextView) findViewById(R.id.Filename);
        A = (VideoView) findViewById(R.id.addcutsvideoview);


        s = (RelativeLayout) findViewById(R.id.btn_leftmirror);
        u = (RelativeLayout) findViewById(R.id.btn_TopMirror);
        s.setBackgroundResource(R.drawable.choose_tool);
        u.setBackgroundColor(Color.TRANSPARENT);
        b = 1;


        o.setText(new File(videoPath).getName());

        videoPlayBtn = findViewById(R.id.videoplaybtn);
        videoPlayBtn.setOnClickListener(view -> d());

        ImageView close = findViewById(R.id.close);
        close.setOnClickListener(view -> onBackPressed());

        ImageView save = findViewById(R.id.save);
        save.setOnClickListener(view -> {
            String[] strArr = new String[0];
            try {
                if (A.isPlaying()) {
                    videoPlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    A.pause();
                }
            } catch (Exception ignored) {
            }
//            String sb = Environment.getExternalStorageDirectory().getAbsoluteFile() +
//                    "/" +
//                    getResources().getString(R.string.MainFolderName) +
//                    "/" +
//                    "Mirror";
            String sb = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                    "/" +
                    getResources().getString(R.string.MainFolderName);
            File file = new File(sb);
            if (!file.exists()) {
                file.mkdirs();
            }
//            C = Environment.getExternalStorageDirectory().getAbsoluteFile() +
//                    "/" +
//                    getResources().getString(R.string.MainFolderName) +
//                    "/" +
//                    "Mirror" +
//                    "/" +
//                    "/Mirror_" +
//                    System.currentTimeMillis() +
//                    ".mp4";

            C = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                    "/" +
                    getResources().getString(R.string.MainFolderName) +
                    "/" +
                    "Editingfy_Mirror_" +
                    System.currentTimeMillis() +
                    ".mp4";
            if (b != 0) {
                if (b == 1) {
                    strArr = cmdleft(videoPath, C);
                    a(strArr, C);
                } else if (b == 2) {
                    strArr = cmdtop(videoPath, C);
                    a(strArr, C);
                }
            } else {
                Toast.makeText(VideoMirrorActivity.this, "Select Your Option", Toast.LENGTH_LONG).show();
            }
        });

        s.setOnClickListener(view -> {
            b = 1;
            s.setBackgroundResource(R.drawable.choose_tool);
            u.setBackgroundColor(Color.TRANSPARENT);
        });
        u.setOnClickListener(view -> {
            b = 2;
            u.setBackgroundResource(R.drawable.choose_tool);
            s.setBackgroundColor(Color.TRANSPARENT);
        });
        videoSeekBar();
    }

    public void videoSeekBar() {
        A.setVideoURI(Uri.parse(videoPath));

        A.setOnPreparedListener(mediaPlayer -> {
            videoPlayBtn.setImageResource(R.drawable.ic_baseline_pause_24);
            A.start();
        });

        A.setOnErrorListener((mediaPlayer, i, i2) -> false);

        videoPlayBtn.setOnClickListener(view -> d());
    }

    private void a(String[] comand, final String str) {

        MediaPlayer mp = MediaPlayer.create(this, Uri.parse(videoPath));
        videoLength = mp.getDuration();
        mp.release();


        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_progress);

        Button cancel = dialog.findViewById(R.id.cancel);
        ProgressBar progBar = dialog.findViewById(R.id.progress_bar);
        progBar.setProgress(0);
//        TextView progText = (TextView) dialog.findViewById(R.id.progress_text_bar);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.arthenica.mobileffmpeg.FFmpeg.cancel(executionId);
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        executionId = FFmpeg.executeAsync(comand, new ExecuteCallback() {
            @Override
            public void apply(long executionId, int returnCode) {
                if (returnCode == RETURN_CODE_SUCCESS) {
                    addVideo(str);
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(VideoMirrorActivity.this, "Execution completed successfully.", Toast.LENGTH_SHORT).show();
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(VideoMirrorActivity.this, "Execution cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(VideoMirrorActivity.this, "Execution failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        Config.enableStatisticsCallback(new StatisticsCallback() {
            public void apply(Statistics newStatistics) {

                float progress = Float.parseFloat(valueOf(newStatistics.getTime())) / videoLength;
                float progressFinal = progress * 100;
                Log.d(com.arthenica.mobileffmpeg.Config.TAG, "Video Length: " + progressFinal);
                Log.d(com.arthenica.mobileffmpeg.Config.TAG, format("frame: %d, time: %d", newStatistics.getVideoFrameNumber(), newStatistics.getTime()));
                Log.d(com.arthenica.mobileffmpeg.Config.TAG, format("Quality: %f, time: %f", newStatistics.getVideoQuality(), newStatistics.getVideoFps()));
                progBar.setProgress((int) progressFinal);
//                progText.setText(format("%d%%", (int) progressFinal));

                String frame = "Frame: " + newStatistics.getVideoFrameNumber();
                String fps = "Fps: " + newStatistics.getVideoFps();
                String quality = "Quality: " + newStatistics.getVideoQuality();
                String time = "Time: " + simpleDateFormat.format(newStatistics.getTime());
                String bitrate = "Bitrate: " + newStatistics.getBitrate();
                String speed = "Speed: " + newStatistics.getSpeed();
                String size = "Size: " + Formatter.formatFileSize(getApplicationContext(), newStatistics.getSize());
            }
        });

    }

    public Uri addVideo(String videoFile) {
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Video.Media.TITLE, "Mirror_" + System.currentTimeMillis());
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Video.Media.DURATION, videoLength);
        }
        values.put(MediaStore.Video.Media.DESCRIPTION, "Mirrored Via Editingfy Video");
        values.put(MediaStore.Video.Media.TAGS, "Editingfy Video");
        values.put(MediaStore.Video.Media.DATA, videoFile);
        return getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

    public String[] cmdleft(String str, String str2) {
        return new String[]{"-i", str, "-vf", "hflip", "-c:a", "copy", str2};
    }

    public String[] cmdtop(String str, String str2) {
        return new String[]{"-i", str, "-vf", "vflip", "-c:a", "copy", str2};
    }

    public void d() {
        if (A.isPlaying()) {
            A.pause();
            videoPlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            return;
        }
        A.start();
        videoPlayBtn.setImageResource(R.drawable.ic_baseline_pause_24);
    }

    @Override
    public void onDestroy() {
        if (A != null) {
            A.stopPlayback();
        }
        super.onDestroy();
    }


    private int e() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i2 = displayMetrics.heightPixels;
        int i3 = displayMetrics.widthPixels;
        return Math.min(i3, i2);
    }


    @Override
    public void onPause() {
        super.onPause();
        c = false;
        try {
            if (A.isPlaying()) {
                videoPlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                A.pause();
            }
        } catch (Exception ignored) {
        }
    }

    public void g() {
        new AlertDialog.Builder(this).setTitle("Device not supported").setMessage("Editingfy is not supported on your device").setCancelable(false).setPositiveButton("Ok", (dialogInterface, i) -> VideoMirrorActivity.this.finish()).create().show();
    }

    public void deleteFromGallery(String str) {
        String[] strArr = {"_id"};
        String[] strArr2 = {str};
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor query = contentResolver.query(uri, strArr, "_data = ?", strArr2, null);
        if (query.moveToFirst()) {
            try {
                contentResolver.delete(ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, query.getLong(query.getColumnIndexOrThrow("_id"))), null, null);
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            }
        } else {
            try {
                boolean delete = new File(str).delete();
                if (delete) {

                    refreshGallery(str);
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        query.close();
    }

    public void refreshGallery(String str) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(str)));
        sendBroadcast(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        this.B = getIntent().getStringExtra("videouri");
        videoPath = this.B;
    }

}
