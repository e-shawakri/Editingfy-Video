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
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.emadyous.editingfyVideos.ui.VideoPlayerState;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static java.lang.String.format;
import static java.lang.String.valueOf;


public class SlowMotionVideoActivity extends AppCompatActivity {
    static final boolean k = true;
    String a;
    String b = null;
    Boolean d = Boolean.FALSE;
    FloatingActionButton e;
    int multiplier;
    StringBuilder i = new StringBuilder();
    int videoLength;
    int galleryVideoLength;

    private TextView p;

    private VideoPlayerState q = new VideoPlayerState();
    private final a r = new a();

    private VideoView s;
    private String output;
    private long executionId;

    private class a extends Handler {
        private boolean b;
        private final Runnable c;

        private a() {
            this.b = false;
            this.c = new Runnable() {
                public void run() {
                    a.this.a();
                }
            };
        }


        public void a() {
            if (!this.b) {
                this.b = SlowMotionVideoActivity.k;
                sendEmptyMessage(0);
            }
        }

        @Override
        public void handleMessage(Message message) {
            this.b = false;
            if (!SlowMotionVideoActivity.this.s.isPlaying()) {
                if (SlowMotionVideoActivity.this.s.isPlaying()) {
                    SlowMotionVideoActivity.this.s.pause();
                    SlowMotionVideoActivity.this.d = Boolean.FALSE;
                    e.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                }
                return;
            }
            postDelayed(this.c, 50);
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_slow_motion);


        d();
        Object lastNonConfigurationInstance = getLastNonConfigurationInstance();
        if (lastNonConfigurationInstance != null) {
            this.q = (VideoPlayerState) lastNonConfigurationInstance;
        } else {
            Bundle extras = getIntent().getExtras();
            this.q.setFilename(extras.getString("videofilename"));
            b = extras.getString("videofilename");
        }
        this.p.setText(new File(this.b).getName());
        this.s.setOnCompletionListener(mediaPlayer -> {
            SlowMotionVideoActivity.this.d = Boolean.FALSE;
            e.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        });
        g();
        SlowMotionVideoActivity.this.c();

    }


    public void c() {
        Intent intent = new Intent(getApplicationContext(), VideoPlayer.class);
        intent.putExtra("song", output);
        startActivity(intent);
        finish();
    }

    public void slowmotioncommand() {

//        String sb = Environment.getExternalStorageDirectory().getAbsoluteFile() +
//                "/" +
//                getResources().getString(R.string.MainFolderName) +
//                "/" +
//                "Slow Motion";
        String sb = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                "/" +
                getResources().getString(R.string.MainFolderName);
        File file = new File(sb);
        if (!file.exists()) {
            file.mkdirs();
        }

        String format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());

//        output = Environment.getExternalStorageDirectory().getAbsoluteFile() +
//                "/" +
//                getResources().getString(R.string.MainFolderName) +
//                "/" +
//                "Slow Motion" +
//                "/" +
//                "Slow_" +
//                format +
//                ".mp4";

        output = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                "/" +
                getResources().getString(R.string.MainFolderName) +
                "/" +
                "Slow_" +
                format +
                ".mp4";


        String[] cmd = new String[0];


            if (multiplier == 2) {
                cmd = new String[]{"-i", b, "-filter_complex", "[0:v]setpts=" + (float) multiplier + "*PTS[v];[0:a]atempo=0.5[a]", "-map", "[v]", "-map", "[a]", "-crf", "27", "-preset", "veryfast", output};
            } else if (multiplier == 4) {
                cmd = new String[]{"-i", b, "-filter_complex", "[0:v]setpts=" + (float) multiplier + "*PTS[v];[0:a]atempo=0.5,atempo=0.5[a]", "-map", "[v]", "-map", "[a]", "-crf", "27", "-preset", "veryfast", output};
            }


        a(cmd, output);
    }



    private void a(String[] comand, final String str) {

        MediaPlayer mp = MediaPlayer.create(this, Uri.parse(b));
        galleryVideoLength = mp.getDuration();


        if (multiplier == 2) {
            videoLength = mp.getDuration() * 2;
            mp.release();
        } else if(multiplier == 4){
            videoLength = mp.getDuration() * 4;
            mp.release();
        }


//        int videoLength = this.s.getDuration();


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
                FFmpeg.cancel(executionId);
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
                    Toast.makeText(SlowMotionVideoActivity.this, "Execution completed successfully.", Toast.LENGTH_SHORT).show();
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(SlowMotionVideoActivity.this, "Execution cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(SlowMotionVideoActivity.this, "Execution failed", Toast.LENGTH_SHORT).show();
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
        values.put(MediaStore.Video.Media.TITLE, "Slow_" + System.currentTimeMillis());
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Video.Media.DURATION, galleryVideoLength);
        }
        values.put(MediaStore.Video.Media.DESCRIPTION, "Slow Motion Via Editingfy Video");
        values.put(MediaStore.Video.Media.TAGS, "Editingfy Video");
        values.put(MediaStore.Video.Media.DATA, videoFile);
        return getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

    private void d() {
        p = (TextView) findViewById(R.id.Filename);
        e = findViewById(R.id.buttonply1);

        RelativeLayout doubleSlow = findViewById(R.id.double_slow);
        RelativeLayout quadreSlow = findViewById(R.id.quadre_slow);

        multiplier = 2;
        doubleSlow.setBackgroundResource(R.drawable.choose_tool);
        quadreSlow.setBackgroundColor(Color.TRANSPARENT);


        doubleSlow.setOnClickListener(view -> {
            multiplier = 2;
            doubleSlow.setBackgroundResource(R.drawable.choose_tool);
            quadreSlow.setBackgroundColor(Color.TRANSPARENT);
        });

        quadreSlow.setOnClickListener(view -> {
            multiplier = 4;
            quadreSlow.setBackgroundResource(R.drawable.choose_tool);
            doubleSlow.setBackgroundColor(Color.TRANSPARENT);
        });

        e.setOnClickListener(view -> {
            if (d) {
                e.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                d = Boolean.FALSE;
            } else {
                e.setImageResource(R.drawable.ic_baseline_pause_24);
                SlowMotionVideoActivity.this.d = Boolean.TRUE;
            }
            SlowMotionVideoActivity.this.h();
        });
        this.s = (VideoView) findViewById(R.id.videoView1);

        findViewById(R.id.close).setOnClickListener(view -> onBackPressed());

        findViewById(R.id.save).setOnClickListener(view -> {
            if (s.isPlaying()) {
                s.pause();
                e.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            }
            slowmotioncommand();
        });
    }

    public void f() {
        new AlertDialog.Builder(this).setTitle("Device not supported").setMessage("Editingfy is not supported on your device").setCancelable(false).setPositiveButton("Ok", (dialogInterface, i) -> SlowMotionVideoActivity.this.finish()).create().show();
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

    private void g() {
        this.s.setOnPreparedListener(mediaPlayer -> {
            mediaPlayer.start();
            e.setImageResource(R.drawable.ic_baseline_pause_24);
            SlowMotionVideoActivity.this.a = SlowMotionVideoActivity.getTimeForTrackFormat(mediaPlayer.getDuration());
        });
        this.s.setVideoPath(this.q.getFilename());
        this.a = getTimeForTrackFormat(this.s.getDuration());
    }


    public void h() {
        if (this.s.isPlaying()) {
            this.s.pause();
            e.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            return;
        }
        this.s.start();
        e.setImageResource(R.drawable.ic_baseline_pause_24);
        this.r.a();
    }

    @SuppressLint("DefaultLocale")
    public static String getTimeForTrackFormat(int i2) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) i2), TimeUnit.MILLISECONDS.toSeconds((long) i2) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) i2)));
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!this.s.isPlaying()) {
            this.d = Boolean.FALSE;
            e.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
        this.s.seekTo(this.q.getCurrentTime());
    }


    @Override
    public void onPause() {
        super.onPause();
        this.q.setCurrentTime(this.s.getCurrentPosition());
    }

}
