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
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Media;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static java.lang.String.format;
import static java.lang.String.valueOf;

public class VideoToMP3ConverterActivity extends AppCompatActivity {
    static final boolean G = true;
    public static String metaFilePath;
    Boolean A = Boolean.FALSE;
    int D;
    VideoView F;
    private TextView fileName;
    String b;
    String c;
    String d;
    String g;
    String videoPath;
    String i = "00";
    public TextView iv_aac;
    public TextView iv_mp3;
    Bundle k;
    FloatingActionButton l;
    ImageView n;
    private final VideoPlayerState videoPlayerState = new VideoPlayerState();
    private long executionId;
    String format;
    int videoLength;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_to_mp3);


        k = getIntent().getExtras();
        D = 1;
        if (this.k != null) {
            videoPath = this.k.getString("videopath");
            String sb = "=== videopath" +
                    this.videoPath;
            Log.e("", sb);
            this.videoPlayerState.setFilename(this.videoPath);
        }
        try {
            thumbVideo(this.videoPath);
        } catch (Exception ignored) {
        }
        F = findViewById(R.id.videoView);
        l = findViewById(R.id.btnPlayVideo);
        fileName = findViewById(R.id.Filename);
        fileName.setText(new File(this.videoPath).getName());
        iv_aac = findViewById(R.id.iv_aac);
        iv_mp3 = findViewById(R.id.iv_mp3);

        d = "MP3";
        iv_mp3.setBackgroundResource(R.drawable.choose_tool);
        iv_aac.setBackgroundColor(Color.TRANSPARENT);

        iv_aac.setOnClickListener(view -> {
            d = "AAC";
            iv_aac.setBackgroundResource(R.drawable.choose_tool);
            iv_mp3.setBackgroundColor(Color.TRANSPARENT);
        });
        iv_mp3.setOnClickListener(view -> {
            d = "MP3";
            iv_mp3.setBackgroundResource(R.drawable.choose_tool);
            iv_aac.setBackgroundColor(Color.TRANSPARENT);
        });
        initVideoView();
        d = "MP3";
        g = "None";
        VideoToMP3ConverterActivity.this.c();


        findViewById(R.id.close).setOnClickListener(view -> onBackPressed());

        findViewById(R.id.save).setOnClickListener(view -> {

//            String sb = Environment.getExternalStorageDirectory().getAbsoluteFile() +
//                    "/" +
//                    getResources().getString(R.string.MainFolderName) +
//                    "/" +
//                    "Audios";

            String sb = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                    "/" +
                    getResources().getString(R.string.MainFolderName) +
                    "/" +
                    "Editingfy Audios";
            File file = new File(sb);
            if (!file.exists()) {
                file.mkdirs();
            }
            format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
            if (F.isPlaying()) {
                F.pause();
                l.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                A = Boolean.FALSE;
            }
            if (d.equals("MP3")) {
                String sb2 = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                        "/" +
                        getResources().getString(R.string.MainFolderName) +
                        "/" +
                        "Editingfy Audios" +
                        "/" +
                        "Editingfy_Audio_" +
                        format +
                        ".mp3";
                c = sb2;
            } else if (d.equals("AAC")) {
                String sb2 = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                        "/" +
                        getResources().getString(R.string.MainFolderName) +
                        "/" +
                        "Editingfy Audios" +
                        "/" +
                        "Editingfy_Audio_" +
                        format +
                        ".aac";
                c = sb2;
            }
            d();
        });

    }


    public void b() {
            c();
        }


    public void c() {
        Intent intent = new Intent(this, AudioPlayer.class);
        Bundle bundle = new Bundle();
        bundle.putString("song", this.c);
        bundle.putBoolean("isfrom", G);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void d() {
        String[] strings = new String[] {
                "-i" , videoPath , "-b:a" , "192K" , "-vn" , c
        };
        a(strings, this.c);
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
                    if (d.equals("MP3")){
                        addMP3(str);
                    } else if (d.equals("AAC")){
                        addAAC(str);
                    }
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(VideoToMP3ConverterActivity.this, "Execution completed successfully.", Toast.LENGTH_SHORT).show();
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(VideoToMP3ConverterActivity.this, "Execution cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(VideoToMP3ConverterActivity.this, "Execution failed", Toast.LENGTH_SHORT).show();
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

    public Uri addMP3(String videoFile) {
        ContentValues values = new ContentValues(4);
        values.put(Audio.Media.TITLE, "Audio_" + format);
        values.put(Audio.Media.MIME_TYPE, "audio/mp3");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(Audio.Media.DURATION, videoLength);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            values.put(Audio.Media.AUTHOR, "Editingfy Video");
        }
        values.put(Audio.Media.DATA, videoFile);
        return getContentResolver().insert(Audio.Media.EXTERNAL_CONTENT_URI, values);
    }

    public Uri addAAC(String videoFile) {
        ContentValues values = new ContentValues(4);
        values.put(Audio.Media.TITLE, "Audio_" + format);
        values.put(Audio.Media.MIME_TYPE, "audio/aac");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(Audio.Media.DURATION, videoLength);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            values.put(Audio.Media.AUTHOR, "Editingfy Video");
        }
        values.put(Audio.Media.DATA, videoFile);
        return getContentResolver().insert(Audio.Media.EXTERNAL_CONTENT_URI, values);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void initVideoView() {
        this.F.setVideoPath(this.videoPath);
        this.F.seekTo(200);
        this.F.setOnErrorListener((mediaPlayer, i, i2) -> {
            Toast.makeText(VideoToMP3ConverterActivity.this.getApplicationContext(), "Video Player Not Supproting", Toast.LENGTH_SHORT).show();
            return VideoToMP3ConverterActivity.G;
        });
        this.F.setOnCompletionListener(mediaPlayer -> {
            VideoToMP3ConverterActivity.this.F.pause();
            l.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            VideoToMP3ConverterActivity.this.A = Boolean.FALSE;
        });
        this.F.setOnTouchListener((view, motionEvent) -> {
            if (VideoToMP3ConverterActivity.this.A) {
                VideoToMP3ConverterActivity.this.F.pause();
                VideoToMP3ConverterActivity.this.A = Boolean.FALSE;
                l.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            }
            return VideoToMP3ConverterActivity.G;
        });
        this.F.setOnPreparedListener(mediaPlayer -> {
            VideoToMP3ConverterActivity.this.b = VideoToMP3ConverterActivity.getTimeForTrackFormat(mediaPlayer.getDuration(), VideoToMP3ConverterActivity.G);
            VideoToMP3ConverterActivity.this.F.seekTo(200);
            VideoToMP3ConverterActivity.this.l.setOnClickListener(view -> {
                if (VideoToMP3ConverterActivity.this.A) {
                    l.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    VideoToMP3ConverterActivity.this.A = Boolean.FALSE;
                } else {
                    l.setImageResource(R.drawable.ic_baseline_pause_24);
                    VideoToMP3ConverterActivity.this.A = Boolean.TRUE;
                }
                VideoToMP3ConverterActivity.this.performVideoViewClick();
            });
        });
        this.b = getTimeForTrackFormat(this.F.getDuration(), G);
    }


    @Override
    public void onResume() {
        super.onResume();
        this.d = "MP3";
        this.g = "None";
    }

    public void thumbVideo(String str) {
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        String[] strArr = {"_data", "_id"};
        StringBuilder sb = new StringBuilder();
        sb.append("%");
        sb.append(str);
        sb.append("%");
        Cursor managedQuery = managedQuery(uri, strArr, "_data  like ?", new String[]{sb.toString()}, " _id DESC");
        int count = managedQuery.getCount();
        String sb2 = "count" +
                count;
        Log.e("", sb2);
        if (count > 0) {
            try {
                managedQuery.moveToFirst();
                for (int i2 = 0; i2 < count; i2++) {
                    try {
                        Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, getLong(managedQuery));
                        managedQuery.getString(managedQuery.getColumnIndex("_data"));
                        Bitmap thumbnail = Thumbnails.getThumbnail(getContentResolver(), managedQuery.getLong(managedQuery.getColumnIndexOrThrow("_id")), 1, null);
                        String sb3 = "Bitmap" +
                                thumbnail;
                        Log.e("", sb3);
                        managedQuery.moveToNext();
                    } catch (IllegalArgumentException e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (IllegalArgumentException e3) {
                e3.printStackTrace();
            }
        }
    }

    public static String getLong(Cursor cursor) {
        return String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
    }

    public static String getTimeForTrackFormat(int i2, boolean z2) {
        int i3 = i2 / 3600000;
        int i4 = i2 / 60000;
        int i5 = (i2 - ((i4 * 60) * 1000)) / 1000;
        StringBuilder sb = new StringBuilder((!z2 || i3 >= 10) ? "" : "0");
        sb.append(i3);
        sb.append(":");
        String sb4 = sb.toString() + ((!z2 || i4 >= 10) ? "" : "0") + i4 % 60 +
                ":";
        if (i5 < 10) {
            String sb5 = sb4 + "0" +
                    i5;
            return sb5;
        }
        return sb4 + i5;
    }

    public void performVideoViewClick() {
        if (this.F.isPlaying()) {
            this.F.pause();
            return;
        }
        this.F.start();
    }

    public void scanMedia(String str) {
        String substring = str.substring(str.lastIndexOf("/") + 1);
        String substring2 = substring.substring(0, substring.lastIndexOf("."));
        ContentValues contentValues = new ContentValues();
        contentValues.put("_data", str);
        contentValues.put("title", substring2);
        contentValues.put("_size", str.length());
        contentValues.put("mime_type", "audio/mp3");
        contentValues.put("artist", getResources().getString(R.string.app_name));
        contentValues.put("is_ringtone", Boolean.TRUE);
        contentValues.put("is_notification", Boolean.FALSE);
        contentValues.put("is_alarm", Boolean.FALSE);
        contentValues.put("is_music", Boolean.FALSE);
        Uri contentUriForPath = Audio.Media.getContentUriForPath(str);
        String sb = "===== Enter ====" +
                contentUriForPath;
        Log.e("", sb);
        ContentResolver contentResolver = getContentResolver();
        String sb2 = "_data=\"" +
                str +
                "\"";
        contentResolver.delete(contentUriForPath, sb2, null);
        getApplicationContext().getContentResolver().insert(contentUriForPath, contentValues);
    }

    @Override
    public void onActivityResult(int i2, int i3, Intent intent) {
        super.onActivityResult(i2, i3, intent);
        if (intent != null) {
            Uri data = intent.getData();
            String sb = "File Path :" +
                    data;
            Log.e("", sb);
            String sb2 = "Final Image Path :" +
                    getRealPathFromURI(data);
            Log.e("", sb2);
            String realPathFromURI = getRealPathFromURI(data);
            metaFilePath = realPathFromURI;
            this.n.setImageBitmap(rotateBitmapOrientation(realPathFromURI));
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor managedQuery = managedQuery(uri, new String[]{"_data"}, null, null, null);
        int columnIndexOrThrow = managedQuery.getColumnIndexOrThrow("_data");
        managedQuery.moveToFirst();
        return managedQuery.getString(columnIndexOrThrow);
    }

    public Bitmap rotateBitmapOrientation(String str) {
        ExifInterface exifInterface;
        Options options = new Options();
        int i2 = 1;
        options.inJustDecodeBounds = G;
        BitmapFactory.decodeFile(str, options);
        Bitmap decodeFile = BitmapFactory.decodeFile(str, new Options());
        try {
            exifInterface = new ExifInterface(str);
        } catch (IOException e2) {
            e2.printStackTrace();
            exifInterface = null;
        }
        assert exifInterface != null;
        String attribute = exifInterface.getAttribute("Orientation");
        if (attribute != null) {
            i2 = Integer.parseInt(attribute);
        }
        int i3 = 0;
        if (i2 == 6) {
            i3 = 90;
        }
        if (i2 == 3) {
            i3 = 180;
        }
        if (i2 == 8) {
            i3 = 270;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate((float) i3, ((float) decodeFile.getWidth()) / 2.0f, ((float) decodeFile.getHeight()) / 2.0f);
        return Bitmap.createBitmap(decodeFile, 0, 0, options.outWidth, options.outHeight, matrix, G);
    }

    public void f() {
        new AlertDialog.Builder(this).setTitle("Device not supported").setMessage("Editingfy is not supported on your device").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                VideoToMP3ConverterActivity.this.finish();
            }
        }).create().show();
    }

    public void deleteFromGallery(String str) {
        String[] strArr = {"_id"};
        String[] strArr2 = {str};
        Uri uri = Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor query = contentResolver.query(uri, strArr, "_data = ?", strArr2, null);
        if (query.moveToFirst()) {
            try {
                contentResolver.delete(ContentUris.withAppendedId(Images.Media.EXTERNAL_CONTENT_URI, query.getLong(query.getColumnIndexOrThrow("_id"))), null, null);
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
