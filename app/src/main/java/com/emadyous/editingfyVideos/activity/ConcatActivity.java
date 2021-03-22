/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.Statistics;
import com.arthenica.mobileffmpeg.StatisticsCallback;
import com.emadyous.editingfyVideos.R;
import com.emadyous.editingfyVideos.adapter.SelectVideoAdapter;
import com.emadyous.editingfyVideos.adapter.VideoFilesAdapter;
import com.emadyous.editingfyVideos.utils.ContentUtill;
import com.emadyous.editingfyVideos.model.VideoData;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static java.lang.String.format;
import static java.lang.String.valueOf;

public class ConcatActivity extends AppCompatActivity implements View.OnClickListener,
        VideoFilesAdapter.VideoFilesHandler, SelectVideoAdapter.OnSelectionChangedListener {

    private List<VideoData> list;
    private String output;
    private long executionId;
    int videoLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concat);


        list = new ArrayList<>();
        RecyclerView videos = findViewById(R.id.videos_rv);
        ImageView close = findViewById(R.id.close);
        ImageView save = findViewById(R.id.save);


        close.setOnClickListener(this);
        save.setOnClickListener(this);

        TextView emptyState = findViewById(R.id.empty_state);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        SelectVideoAdapter adapter = new SelectVideoAdapter(getVideos());
        adapter.setSelectedBackground(R.drawable.video_selector);

        videos.setAdapter(adapter);
        videos.setLayoutManager(manager);

        SelectionTracker<Long> mSelectionTracker = new SelectionTracker.Builder<>(
                "selection",
                videos,
                new SelectVideoAdapter.KeyProvider(adapter),
                new SelectVideoAdapter.DetailsLookup(videos),
                StorageStrategy.createLongStorage()
        )
                .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build();


        adapter.setmSelectionTracker(mSelectionTracker, this);


        if (!getVideos().isEmpty()) {
            videos.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.INVISIBLE);
        } else {
            videos.setVisibility(View.INVISIBLE);
            emptyState.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onStop() {
        FFmpeg.cancel(executionId);
        super.onStop();
    }

    private void mergeVideos(List<VideoData> list) {
//        String sb = Environment.getExternalStorageDirectory().getAbsoluteFile() +
//                "/" +
//                getResources().getString(R.string.MainFolderName) +
//                "/" +
//                "Merge";
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
                "Merge_" +
                format +
                ".mp4";

        //[v2][2:a]

        String[] complexCommand;


            if (list.size() == 2) {
                complexCommand = new String[]{"-y", "-i", list.get(0).videoPath, "-i", list.get(1).videoPath, "-strict", "experimental", "-filter_complex",
                        "[0:v]scale=720x480,setsar=1[v0];[1:v]scale=720x480,setsar=1[v1];[v0][0:a][v1][1:a] concat=n=2:v=1:a=1;",
                        "-ab", "48000", "-ac", "2", "-ar", "22050", "-crf","27","-preset", "ultrafast", output};
                a(complexCommand, output);
            } else if (list.size() == 3){
                complexCommand = new String[]{"-y", "-i", list.get(0).videoPath, "-i", list.get(1).videoPath, "-i", list.get(2).videoPath, "-strict", "experimental", "-filter_complex",
                        "[0:v]scale=720x480,setsar=1[v0];[1:v]scale=720x480,setsar=1[v1];[2:v]scale=720x480,setsar=1[v2];[v0][0:a][v1][1:a][v2][2:a] concat=n=3:v=1:a=1;",
                        "-ab", "48000", "-ac", "2", "-ar", "22050", "-crf","27","-preset", "ultrafast", output};
                a(complexCommand, output);
            } else if (list.size() == 4){
                complexCommand = new String[]{"-y", "-i", list.get(0).videoPath, "-i", list.get(1).videoPath, "-i", list.get(2).videoPath, "-i", list.get(3).videoPath, "-strict", "experimental", "-filter_complex",
                        "[0:v]scale=720x480,setsar=1[v0];[1:v]scale=720x480,setsar=1[v1];[2:v]scale=720x480,setsar=1[v2];[3:v]scale=720x480,setsar=1[v3];[v0][0:a][v1][1:a][v2][2:a][v3][3:a] concat=n=4:v=1:a=1;",
                        "-ab", "48000", "-ac", "2", "-ar", "22050", "-crf","27","-preset", "ultrafast", output};
                a(complexCommand, output);
            } else {
                Toast.makeText(this, "You can do 4 videos at max.", Toast.LENGTH_SHORT).show();
            }



    }

    private List<VideoData> getVideos() {
        List<VideoData> list = new ArrayList<>();
        Cursor managedQuery = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "_id", "_display_name", "duration"}, null, null, " _id DESC");
        int count = managedQuery.getCount();
        managedQuery.moveToFirst();
        for (int i = 0; i < count; i++) {
            Uri withAppendedPath = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, ContentUtill.getLong(managedQuery));
            list.add(new VideoData(managedQuery.getString(managedQuery.getColumnIndexOrThrow("_display_name")), withAppendedPath, managedQuery.getString(managedQuery.getColumnIndex("_data")), ContentUtill.getTime(managedQuery, "duration")));
            managedQuery.moveToNext();
        }
        return list;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.close) {
            onBackPressed();
        } else if (id == R.id.save) {
            if (!list.isEmpty()) mergeVideos(list);
        }
    }

    @Override
    public void onChoose(int position) {
        Log.d("TAG", "onChoose: " + position);
    }

    public void b() {
            c();

    }

    @Override
    public void onSelectionChanged(VideoData videoData, long position, boolean isSelected) {
        if (isSelected) {
            list.add(videoData);
        } else {
            list.remove(videoData);
        }


        for (VideoData v : list) {
            Log.d("TAG", "onSelectionChanged: " + v.videoPath);
        }

    }


    private void a(String[] comand, final String output) {

        if(list.size() == 2){
            MediaPlayer mp = MediaPlayer.create(this, Uri.parse(list.get(0).videoPath));
            int videoLength1 = mp.getDuration();
            mp.release();

            MediaPlayer mp1 = MediaPlayer.create(this, Uri.parse(list.get(1).videoPath));
            int videoLength2 = mp1.getDuration();
            mp1.release();

             videoLength = videoLength1 + videoLength2;

        } else if(list.size() == 3){
            MediaPlayer mp = MediaPlayer.create(this, Uri.parse(list.get(0).videoPath));
            int videoLength1 = mp.getDuration();
            mp.release();

            MediaPlayer mp1 = MediaPlayer.create(this, Uri.parse(list.get(1).videoPath));
            int videoLength2 = mp1.getDuration();
            mp1.release();

            MediaPlayer mp2 = MediaPlayer.create(this, Uri.parse(list.get(2).videoPath));
            int videoLength3 = mp2.getDuration();
            mp2.release();

            videoLength = videoLength1 + videoLength2 + videoLength3;

        } else if(list.size() == 4){
            MediaPlayer mp = MediaPlayer.create(this, Uri.parse(list.get(0).videoPath));
            int videoLength1 = mp.getDuration();
            mp.release();

            MediaPlayer mp1 = MediaPlayer.create(this, Uri.parse(list.get(1).videoPath));
            int videoLength2 = mp1.getDuration();
            mp1.release();

            MediaPlayer mp2 = MediaPlayer.create(this, Uri.parse(list.get(2).videoPath));
            int videoLength3 = mp2.getDuration();
            mp2.release();

            MediaPlayer mp3 = MediaPlayer.create(this, Uri.parse(list.get(3).videoPath));
            int videoLength4 = mp3.getDuration();
            mp3.release();

            videoLength = videoLength1 + videoLength2 + videoLength3 + videoLength4;

        }

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
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(ConcatActivity.this, "Execution completed successfully.", Toast.LENGTH_SHORT).show();
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(ConcatActivity.this, "Execution cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(ConcatActivity.this, "Execution failed", Toast.LENGTH_SHORT).show();
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

    public void c() {
        Intent intent = new Intent(this, VideoPlayer.class);
        intent.putExtra("song", output);
        startActivity(intent);
    }

    public void refreshGallery(String str) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(str)));
        sendBroadcast(intent);
    }

    public void deleteFromGallery(String str) {
        String[] strArr = {"_id"};
        String[] strArr2 = {str};
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor query = contentResolver.query(uri, strArr, "_data = ?", strArr2, null);
        if (query.moveToFirst()) {
            try {
                contentResolver.delete(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, query.getLong(query.getColumnIndexOrThrow("_id"))), null, null);
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


    public void g() {
        new AlertDialog.Builder(this).setTitle("Device not supported").setMessage("Editingfy is not supported on your device").setCancelable(false).setPositiveButton("Ok", (dialogInterface, i) -> finish()).create().show();
    }

}