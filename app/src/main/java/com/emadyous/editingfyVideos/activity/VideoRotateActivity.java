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
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.Statistics;
import com.arthenica.mobileffmpeg.StatisticsCallback;
import com.emadyous.editingfyVideos.R;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.feeeei.circleseekbar.CircleSeekBar;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static java.lang.String.format;
import static java.lang.String.valueOf;

public class VideoRotateActivity extends AppCompatActivity {
    OnClickListener E = view -> e();

    FloatingActionButton a;
    String f;
    String g = "90";
    String i;
    String j = "";
    boolean m = false;
    Context n;
    TextView s;
    private MediaPlayer mediaPlayer;
    private TextureView textureView;
    private long executionId;
    int videoLength;
    String format;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_rotate);



        Intent intent = getIntent();
        n = this;
        j = intent.getStringExtra("videoPath");
        textureView = findViewById(R.id.vvScreen);
        MediaScannerConnection.scanFile(this, new String[]{new File(this.j).getAbsolutePath()}, new String[]{this.f}, null);
        d();
        a = findViewById(R.id.btnPlayVideo);
        a.setOnClickListener(E);
        s = (TextView) findViewById(R.id.Filename);
        s.setText(new File(j).getName());

        ImageView save = findViewById(R.id.save);
        ImageView close = findViewById(R.id.close);
        save.setOnClickListener(view -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                a.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            }
            rotatecommand();
        });

        close.setOnClickListener(view -> onBackPressed());

        TextView degreeText = findViewById(R.id.degree_text);
        CircleSeekBar circleSeekBar = findViewById(R.id.degree);
        circleSeekBar.setOnSeekBarChangeListener((seekbar, curValue) -> {
            g = String.valueOf(curValue);
            degreeText.setText(curValue + "°");
            textureView.setRotation(curValue);
        });


       VideoRotateActivity.this.c();

    }



    public void b() {
            c();

    }


    public void c() {
        Intent intent = new Intent(getApplicationContext(), VideoPlayer.class);
        intent.putExtra("song", this.i);
        startActivity(intent);
        finish();
    }

    public void rotatecommand() {
        format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
//
        String sb = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                "/" +
                getResources().getString(R.string.MainFolderName);
        File file = new File(sb);
        if (!file.exists()) {
            file.mkdirs();
        }

        i = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                "/" +
                getResources().getString(R.string.MainFolderName) +
                "/" +
                "Editingfy_Rotate_" +
                format +
                ".mp4";

        Log.d("TAG", "rotatecommand: " + j + " " + i);
        String sb3 = "rotate=" +
                g +
                "*PI/180";


        a(new String[]{"-y", "-i", j, "-filter_complex", sb3, "-c:a", "copy", i}, i);


    }


    private void a(String[] comand, final String str) {


        MediaPlayer mp = MediaPlayer.create(this, Uri.parse(j));
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
                    Toast.makeText(VideoRotateActivity.this, "Execution completed successfully.", Toast.LENGTH_SHORT).show();
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(VideoRotateActivity.this, "Execution cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(VideoRotateActivity.this, "Execution failed", Toast.LENGTH_SHORT).show();
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
        values.put(MediaStore.Video.Media.TITLE, "Rotate_" + format);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Video.Media.DURATION, videoLength);
        }
        values.put(MediaStore.Video.Media.DESCRIPTION, "Rotated Via Editingfy Video");
        values.put(MediaStore.Video.Media.TAGS, "Editingfy Video");
        values.put(MediaStore.Video.Media.DATA, videoFile);
        return getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

    private void adjustAspectRatio(int videoWidth, int videoHeight) {
        int viewWidth = textureView.getWidth();
        int viewHeight = textureView.getHeight();
        double aspectRatio = (double) videoHeight / videoWidth;

        int newWidth;
        int newHeight;
        if (viewHeight > (int) (viewWidth * aspectRatio)) {
            // limited by narrow width; restrict height
            newWidth = viewWidth;
            newHeight = (int) (viewWidth * aspectRatio);
        } else {
            // limited by short height; restrict width
            newWidth = (int) (viewHeight / aspectRatio);
            newHeight = viewHeight;
        }
        int xoff = (viewWidth - newWidth) / 2;
        int yoff = (viewHeight - newHeight) / 2;
        Log.v("TAG", "video=" + videoWidth + "x" + videoHeight +
                " view=" + viewWidth + "x" + viewHeight +
                " newView=" + newWidth + "x" + newHeight +
                " off=" + xoff + "," + yoff);

        Matrix txform = new Matrix();
        textureView.getTransform(txform);
        txform.setScale((float) newWidth / viewWidth, (float) newHeight / viewHeight);
        //txform.postRotate(10);          // just for fun
        txform.postTranslate(xoff, yoff);
        textureView.setTransform(txform);
    }

    private void d() {

        mediaPlayer = new MediaPlayer();
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
                Surface surface = new Surface(surfaceTexture);
                try {
                    mediaPlayer.setSurface(surface);
                    mediaPlayer.setDataSource(j);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(mp -> {
                        adjustAspectRatio(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
                        a.setImageResource(R.drawable.ic_baseline_pause_24);
                        mediaPlayer.start();
                    });
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {

                //nada
            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

                //nada
            }
        });

    }


    public void e() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            this.a.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            return;
        }
        mediaPlayer.start();
        this.a.setImageResource(R.drawable.ic_baseline_pause_24);
    }

    public void g() {
        new AlertDialog.Builder(this).setTitle("Device not supported").setMessage("Editingfy is not supported on your device").setCancelable(false).setPositiveButton("Ok", (dialogInterface, i) -> VideoRotateActivity.this.finish()).create().show();
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
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        super.onDestroy();
    }

}
