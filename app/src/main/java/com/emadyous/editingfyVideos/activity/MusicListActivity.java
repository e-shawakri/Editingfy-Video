/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emadyous.editingfyVideos.R;
import com.emadyous.editingfyVideos.adapter.AudioFilesAdapter;
import com.emadyous.editingfyVideos.model.AudioData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class MusicListActivity extends AppCompatActivity implements AudioFilesAdapter.VideoFilesHandler {


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_music_list);


        RecyclerView audios = findViewById(R.id.audios_rv);
        TextView emptyState = findViewById(R.id.empty_state);

        ImageView close = findViewById(R.id.close);
        close.setOnClickListener(view -> onBackPressed());

        if (!getAudios().isEmpty()) {
            emptyState.setVisibility(View.INVISIBLE);
            AudioFilesAdapter adapter = new AudioFilesAdapter(getAudios(), this);
            LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            audios.setLayoutManager(manager);
            audios.setAdapter(adapter);
        } else {
            emptyState.setVisibility(View.VISIBLE);
        }

    }

//    @RequiresApi(api = Build.VERSION_CODES.R)
    private List<AudioData> getAudios() {
        List<AudioData> songs = new ArrayList<>();

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };

        Cursor cursor = this.managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);

        while(cursor.moveToNext()){
            songs.add(new AudioData(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    getTime(cursor.getString(5))));
        }

        return songs;
    }

    private String getTime(String millis) {
        if (millis != null) {
            long duration = Integer.parseInt(millis);
            DateFormat formatter;
            if (duration >= 3600000) {
                formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
            } else {
                formatter = new SimpleDateFormat("mm:ss", Locale.US);
            }
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            return formatter.format(new Date(duration));
        } else {
            return "?";
        }
    }

    @Override
    public void onChoose(int position, String path, String artist) {
        Log.d("TAG", "onChoose: " + position);
        Intent intent = new Intent(MusicListActivity.this, AudioPlayer.class);
        intent.putExtra("song", path);
        intent.putExtra("artist", artist);
        startActivity(intent);
        finish();
    }
}
