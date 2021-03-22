/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emadyous.editingfyVideos.R;
import com.emadyous.editingfyVideos.adapter.VideoFilesAdapter;
import com.emadyous.editingfyVideos.utils.ContentUtill;
import com.emadyous.editingfyVideos.model.VideoData;


import java.util.ArrayList;
import java.util.List;


public class VideoListActivity extends AppCompatActivity implements View.OnClickListener, VideoFilesAdapter.VideoFilesHandler {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_video_list);


        RecyclerView videos = findViewById(R.id.videos_rv);
        ImageView close = findViewById(R.id.close);

        close.setOnClickListener(this);


        TextView emptyState = findViewById(R.id.empty_state);

        if (!getVideos().isEmpty()) {
            emptyState.setVisibility(View.INVISIBLE);
            VideoFilesAdapter adapter = new VideoFilesAdapter(getVideos(), this);
            GridLayoutManager manager = new GridLayoutManager(this, 2);
            videos.setAdapter(adapter);
            videos.setLayoutManager(manager);
        } else {
            emptyState.setVisibility(View.VISIBLE);
        }


    }

    private List<VideoData> getVideos() {
        List<VideoData> list = new ArrayList<>();
        Cursor managedQuery = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "_id", "_display_name", "duration"}, null, null , " _id DESC");
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
        if (view.getId() == R.id.close) {
            onBackPressed();
        }
    }

    private int getToolId() {
        return getIntent().getIntExtra("toolId", 1);
    }

    private void sendUserToTool(int position) {
        int toolId = getToolId();// Tools Activity tools
        if (toolId == 1) {
            Intent cut = new Intent(this, VideoCutter.class);
            cut.putExtra("path", getVideos().get(position).videoPath);
            startActivity(cut);
        } else if (toolId == 11) {
            Intent crop = new Intent(this, VideoCropActivity.class);
            crop.putExtra("videofilename", getVideos().get(position).videoPath);
            startActivity(crop);
        } else if (toolId == 2) {
            Intent mute = new Intent(this, VideoCompressor.class);
            mute.putExtra("videouri", getVideos().get(position).videoPath);
            startActivity(mute);
        } else if (toolId == 13) {
            Intent rotate = new Intent(this, VideoRotateActivity.class);
            rotate.putExtra("videoPath", getVideos().get(position).videoPath);
            startActivity(rotate);
        } else if (toolId == 14) {
            Intent mirror = new Intent(this, VideoMirrorActivity.class);
            mirror.putExtra("videouri", getVideos().get(position).videoPath);
            startActivity(mirror);
        } else if (toolId == 16) {
            Intent reverse = new Intent(this, VideoReverseActivity.class);
            reverse.putExtra("videouri", getVideos().get(position).videoPath);
            startActivity(reverse);
            // Converters Activity tools
        } else if (toolId == 7) {
            Intent toImage = new Intent(this, VideoToImageActivity.class);
            toImage.putExtra("videouri", getVideos().get(position).videoPath);
            startActivity(toImage);
        } else if (toolId == 3) {
            Intent toMp3 = new Intent(this, VideoToMP3ConverterActivity.class);
            toMp3.putExtra("videopath", getVideos().get(position).videoPath);
            startActivity(toMp3);
        } else if (toolId == 12) {
            Intent toGif = new Intent(this, VideoToGIFActivity.class);
            toGif.putExtra("videoPath", getVideos().get(position).videoPath);
            startActivity(toGif);
        } else if (toolId == 9) {
            Intent slow = new Intent(this, SlowMotionVideoActivity.class);
            slow.putExtra("videofilename", getVideos().get(position).videoPath);
            startActivity(slow);
        } else if (toolId == 10) {
            Intent fast = new Intent(this, FastMotionVideoActivity.class);
            fast.putExtra("videofilename", getVideos().get(position).videoPath);
            startActivity(fast);
        } else if (toolId == 1000) {
            Intent videoPlayer = new Intent(this, VideoPlayer.class);
            videoPlayer.putExtra("song", getVideos().get(position).videoPath);
            startActivity(videoPlayer);
        }
    }

    @Override
    public void onChoose(int position) {
        sendUserToTool(position);
        finish();
        Log.d("TAG", "onChoose: " + position);
    }
}
