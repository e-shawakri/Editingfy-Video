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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.emadyous.editingfyVideos.ui.VideoPlayerState;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static java.lang.String.format;
import static java.lang.String.valueOf;

public class FastMotionVideoActivity extends AppCompatActivity {
    String a;
    String b = null;
    Boolean c = Boolean.FALSE;
    FloatingActionButton e;
    int h;
    int videoLength;
    int galleryVideoLength;

    private TextView o;

    private VideoPlayerState p = new VideoPlayerState();

    private VideoView r;
    private String output;
    private long executionId;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_fast_motion);




        d();
        Object lastNonConfigurationInstance = getLastNonConfigurationInstance();
        if (lastNonConfigurationInstance != null) {
            this.p = (VideoPlayerState) lastNonConfigurationInstance;
        } else {
            Bundle extras = getIntent().getExtras();
            this.p.setFilename(extras.getString("videofilename"));
            this.b = extras.getString("videofilename");
        }
        this.o.setText(new File(this.b).getName());
        this.r.setOnCompletionListener(mediaPlayer -> {
            FastMotionVideoActivity.this.c = Boolean.FALSE;
            e.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        });
        g();
         FastMotionVideoActivity.this.c();

    }



    public void c() {
        Intent intent = new Intent(this, VideoPlayer.class);
        Log.d("TAG", "c: " + output);
        intent.putExtra("song", output);
        startActivity(intent);
        finish();
    }

    public void fastmotioncommand() {
//        String sb = Environment.getExternalStorageDirectory().getAbsoluteFile() +
//            "/" +
//            getResources().getString(R.string.MainFolderName) +
//            "/" +
//            "Fast Motion";
        String sb = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                "/" +
                getResources().getString(R.string.MainFolderName);
        File file = new File(sb);
        if (!file.exists()) {
            file.mkdirs();
        }

        String format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());

        output = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                "/" +
                getResources().getString(R.string.MainFolderName) +
                "/" +
                "Editingfy_Fast_" +
                format +
                ".mp4";


        String[] cmd = new String[0];

        //ffmpeg -i input.mkv -filter_complex "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]" -map "[v]" -map "[a]" output.mkv


            if (h == 2) {
                cmd = new String[]{"-i", b, "-filter_complex", "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]" , "-map" , "[v]" , "-map" , "[a]", output};
            } else if (h == 4) {
                cmd = new String[]{"-i", b, "-filter_complex", "[0:v]setpts=0.5*PTS,setpts=0.5*PTS[v];[0:a]atempo=2.0,atempo=2.0[a]" , "-map" , "[v]" , "-map" , "[a]", output};
            }


        a(cmd, output);
    }


    private void a(String[] comand, final String str) {

        MediaPlayer mp = MediaPlayer.create(this, Uri.parse(b));

        galleryVideoLength = mp.getDuration();

        if (h == 2) {
            videoLength = mp.getDuration() / 2;
            mp.release();
        } else if(h == 4){
            videoLength = mp.getDuration() / 4;
            mp.release();
        }


        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_progress);

        Button cancel = dialog.findViewById(R.id.cancel);
        ProgressBar progBar = dialog.findViewById(R.id.progress_bar);
        progBar.setProgress(0);



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
                    Toast.makeText(FastMotionVideoActivity.this, "Execution completed successfully.", Toast.LENGTH_SHORT).show();
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(FastMotionVideoActivity.this, "Execution cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(FastMotionVideoActivity.this, "Execution failed", Toast.LENGTH_SHORT).show();
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
        values.put(MediaStore.Video.Media.TITLE, "Fast_" + System.currentTimeMillis());
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Video.Media.DURATION, galleryVideoLength);
        }
        values.put(MediaStore.Video.Media.DESCRIPTION, "Fast Motion Via Editingfy Video");
        values.put(MediaStore.Video.Media.TAGS, "Editingfy Video");
        values.put(MediaStore.Video.Media.DATA, videoFile);
        return getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

    private void d() {
        o = (TextView) findViewById(R.id.Filename);
        e = findViewById(R.id.buttonply1);

        RelativeLayout doubleFast = findViewById(R.id.double_fast);
        RelativeLayout quadreFast = findViewById(R.id.quadre_fast);

        h = 2;
        doubleFast.setBackgroundResource(R.drawable.choose_tool);
        quadreFast.setBackgroundColor(Color.TRANSPARENT);

        doubleFast.setOnClickListener(view -> {
            h = 2;
            doubleFast.setBackgroundResource(R.drawable.choose_tool);
            quadreFast.setBackgroundColor(Color.TRANSPARENT);
        });

        quadreFast.setOnClickListener(view -> {
            h = 4;
            quadreFast.setBackgroundResource(R.drawable.choose_tool);
            doubleFast.setBackgroundColor(Color.TRANSPARENT);
        });

        ImageView close = findViewById(R.id.close);
        close.setOnClickListener(view -> onBackPressed());

        ImageView save = findViewById(R.id.save);
        save.setOnClickListener(view -> {
            if (r.isPlaying()) {
                r.pause();
                e.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            }
            fastmotioncommand();
        });

        this.e.setOnClickListener(view -> {
            if (c) {
                e.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                FastMotionVideoActivity.this.c = Boolean.FALSE;
            } else {
                e.setImageResource(R.drawable.ic_baseline_pause_24);
                FastMotionVideoActivity.this.c = Boolean.TRUE;
            }
            FastMotionVideoActivity.this.h();
        });
        this.r = (VideoView) findViewById(R.id.videoView1);
    }


    public void f() {
        new AlertDialog.Builder(this).setTitle("Device not supported").setMessage("Editingfy is not supported on your device").setCancelable(false).setPositiveButton("Ok", (dialogInterface, i) -> FastMotionVideoActivity.this.finish()).create().show();
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
        this.r.setOnPreparedListener(mediaPlayer -> {
            mediaPlayer.start();
            e.setImageResource(R.drawable.ic_baseline_pause_24);
        });
        this.r.setVideoPath(this.p.getFilename());
        this.a = getTimeForTrackFormat(this.r.getDuration());
    }


    public void h() {
        if (this.r.isPlaying()) {
            this.r.pause();
            return;
        }
        this.r.start();
    }

    @SuppressLint("DefaultLocale")
    public static String getTimeForTrackFormat(int i2) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) i2), TimeUnit.MILLISECONDS.toSeconds((long) i2) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) i2)));
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!this.r.isPlaying()) {
            this.c = Boolean.FALSE;
            e.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
        this.r.seekTo(this.p.getCurrentTime());
    }

    @Override
    public void onPause() {
        super.onPause();
        this.p.setCurrentTime(this.r.getCurrentPosition());
    }

}
