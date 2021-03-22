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
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.emadyous.editingfyVideos.utils.VideoSliceSeekBar;
import com.emadyous.editingfyVideos.utils.VideoSliceSeekBar.SeekBarChangeListener;
import com.emadyous.editingfyVideos.utils.VideoPlayerState;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static java.lang.String.*;

public class VideoCutter extends AppCompatActivity implements MediaScannerConnectionClient, OnClickListener {
    static final boolean k = true;
    MediaScannerConnection a;
    int b = 0;
    int c = 0;
    TextView d;
    TextView e;
    TextView f;
    TextView g;
    FloatingActionButton play;
    VideoSliceSeekBar seekBar;
    VideoView j;
    private String m;

    private String n;

    private VideoPlayerState o = new VideoPlayerState();
    private long executionId;

    int videoLength = 0;
    String format;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_cut);


        play = findViewById(R.id.buttonply1);
        this.seekBar = (VideoSliceSeekBar) findViewById(R.id.seek_bar1);
        this.f = (TextView) findViewById(R.id.Filename);
        this.d = (TextView) findViewById(R.id.left_pointer);
        this.e = (TextView) findViewById(R.id.right_pointer);
        this.g = (TextView) findViewById(R.id.dur);
        this.j = (VideoView) findViewById(R.id.videoView1);

        ImageView close = findViewById(R.id.close);
        ImageView save = findViewById(R.id.save);
        close.setOnClickListener(this);
        save.setOnClickListener(this);
        this.play.setOnClickListener(this);


        this.m = getIntent().getStringExtra("path");
        if (this.m == null) {
            finish();
        }
        this.f.setText(new File(this.m).getName());
        this.j.setVideoPath(this.m);
        this.j.seekTo(100);
        e();
        this.j.setOnCompletionListener(mediaPlayer -> VideoCutter.this.play.setImageResource(R.drawable.ic_baseline_play_arrow_24));
    }

    public void c() {
        Intent intent = new Intent(getApplicationContext(), VideoPlayer.class);
        intent.putExtra("song", this.n);
        startActivity(intent);
        finish();
    }

    private void d() {

        String valueOf = valueOf(c);
        String valueOf2 = valueOf(b - c);
        format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
//        String sb = Environment.getExternalStorageDirectory().getAbsoluteFile() +
//                "/" +
//                getResources().getString(R.string.MainFolderName) +
//                "/" +
//                "Cut";
        String sb = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                "/" +
                getResources().getString(R.string.MainFolderName);
        File file = new File(sb);
        if (!file.exists()) {
            file.mkdirs();
        }
//        n = Environment.getExternalStorageDirectory().getAbsoluteFile() +
//                "/" +
//                getResources().getString(R.string.MainFolderName) +
//                "/" +
//                "Cut" +
//                "/" +
//                "Cut" +
//                format +
//                ".mp4";

        n = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                "/" +
                getResources().getString(R.string.MainFolderName) +
                "/" +
                "Editingfy_Cut_" +
                format +
                ".mp4";

        a(new String[]{"-ss", valueOf, "-t", valueOf2 , "-i", m,  n}, this.n);

    }


    private void a(String[] comand, final String str) {


        videoLength = (b - c) * 1000;


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
                    Toast.makeText(VideoCutter.this, "Execution completed successfully.", Toast.LENGTH_SHORT).show();
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(VideoCutter.this, "Execution cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(VideoCutter.this, "Execution failed", Toast.LENGTH_SHORT).show();
                }
            }

        });

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        Config.enableStatisticsCallback(new StatisticsCallback() {

            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            public void apply(Statistics newStatistics) {

                float progress = Float.parseFloat(valueOf(newStatistics.getTime())) / videoLength;
                float progressFinal = progress * 100;
                Log.d(Config.TAG, "Video Length: " + progressFinal);
                Log.d(Config.TAG, format("frame: %d, time: %d", newStatistics.getVideoFrameNumber(), newStatistics.getTime()));
                Log.d(Config.TAG, format("Quality: %f, time: %f", newStatistics.getVideoQuality(), newStatistics.getVideoFps()));
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
        values.put(MediaStore.Video.Media.TITLE, "Cut_" + format);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Video.Media.DURATION, videoLength);
        }
        values.put(MediaStore.Video.Media.DESCRIPTION, "Cutted Via Editingfy Video");
        values.put(MediaStore.Video.Media.TAGS, "Editingfy Video");
        values.put(MediaStore.Video.Media.DATA, videoFile);
        return getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

    private void e() {
        this.j.setOnPreparedListener((OnPreparedListener) mediaPlayer -> {
            VideoCutter.this.seekBar.setSeekBarChangeListener(new SeekBarChangeListener() {
                @SuppressLint("DefaultLocale")
                public void SeekBarValueChanged(int i, int i2) {
                    if (VideoCutter.this.seekBar.getSelectedThumb() == 1) {
                        VideoCutter.this.j.seekTo(VideoCutter.this.seekBar.getLeftProgress());
                    }
                    VideoCutter.this.d.setText(formatTimeUnit((long) i));
                    VideoCutter.this.e.setText(formatTimeUnit((long) i2));
                    VideoCutter.this.o.setStart(i);
                    VideoCutter.this.o.setStop(i2);
                    VideoCutter.this.c = i / 1000;
                    VideoCutter.this.b = i2 / 1000;
                    TextView textView = VideoCutter.this.g;
                    textView.setText(format("%02d:%02d:%02d", (VideoCutter.this.b - VideoCutter.this.c) / 3600, ((VideoCutter.this.b - VideoCutter.this.c) % 3600) / 60, (VideoCutter.this.b - VideoCutter.this.c) % 60));
                }
            });
            VideoCutter.this.seekBar.setMaxValue(mediaPlayer.getDuration());
            VideoCutter.this.seekBar.setLeftProgress(0);
            VideoCutter.this.seekBar.setRightProgress(mediaPlayer.getDuration());
            VideoCutter.this.seekBar.setProgressMinDiff(0);
        });
    }

    private void f() {
        if (this.j.isPlaying()) {
            this.j.pause();
            this.seekBar.setSliceBlocked(false);
            this.play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            this.seekBar.removeVideoStatusThumb();
            return;
        }
        this.j.seekTo(this.seekBar.getLeftProgress());
        this.j.start();
        this.seekBar.videoPlayingProgress(this.seekBar.getLeftProgress());
        this.play.setImageResource(R.drawable.ic_baseline_pause_24);
    }

    @SuppressLint("DefaultLocale")
    public static String formatTimeUnit(long j2) {
        return format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(j2), TimeUnit.MILLISECONDS.toSeconds(j2) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j2)));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonply1) {
            f();
        } else if (id == R.id.close) {
            onBackPressed();
        } else if (id == R.id.save) {
            if (this.j.isPlaying()) {
                this.j.pause();
                this.play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            }
            d();
        }
    }

    @Override
    public void onMediaScannerConnected()  {
        String l = "";
        this.a.scanFile(l, "video/*");
    }

    @Override
    public void onScanCompleted(String str, Uri uri) {
        this.a.disconnect();
    }


    public void h() {
        new AlertDialog.Builder(this).setTitle("Device not supported").setMessage("Editingfy is not supported on your device").setCancelable(false).setPositiveButton("Ok", (dialogInterface, i) -> VideoCutter.this.finish()).create().show();
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

}
