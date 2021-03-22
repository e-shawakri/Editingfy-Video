/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.emadyous.editingfyVideos.R;
import com.emadyous.editingfyVideos.adapter.GifsFilesAdapter;
import com.emadyous.editingfyVideos.model.GifData;
import com.emadyous.editingfyVideos.utils.ContentUtill;

import java.util.ArrayList;
import java.util.List;


public class GifListActivity extends AppCompatActivity implements GifsFilesAdapter.VideoFilesHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_list);


        RecyclerView gifs = findViewById(R.id.gifs_rv);
        TextView empty = findViewById(R.id.empty_state);
        ImageView close = findViewById(R.id.close);

        close.setOnClickListener(view -> onBackPressed());

        if (!getGIFs().isEmpty()) {
            empty.setVisibility(View.INVISIBLE);
            GifsFilesAdapter adapter = new GifsFilesAdapter(getGIFs(), this);
            GridLayoutManager manager = new GridLayoutManager(this, 2);
            gifs.setAdapter(adapter);
            gifs.setLayoutManager(manager);
        } else {
            empty.setVisibility(View.VISIBLE);
        }



    }

    private List<GifData> getGIFs() {
        List<GifData> list = new ArrayList<>();

        String[] g = new String[]{"_data", "_size", "_id"};
        String e = "_data like?";
        String sb2 = "%" +
                getResources().getString(R.string.MainFolderName) +
                "/" +
                "GIF" +
                "%";
        String[] f = new String[]{sb2};

        Cursor managedQuery = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, g, e, f, " _id DESC");
        int count = managedQuery.getCount();
        managedQuery.moveToFirst();
        for (int i2 = 0; i2 < count; i2++) {
            Uri withAppendedPath = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentUtill.getLong(managedQuery));
            String string = managedQuery.getString(managedQuery.getColumnIndexOrThrow("_data"));
            String sb3 = managedQuery.getString(managedQuery.getColumnIndex("_size")) +
                    " KB";
            list.add(new GifData(string, withAppendedPath, string, sb3));
            managedQuery.moveToNext();
        }

        return list;
    }

    @Override
    public void onChoose(int position, String path, String name) {
        Log.d("TAG", "onChoose: " + position);
        Intent intent = new Intent(GifListActivity.this, GIFPreviewActivity.class);
        intent.putExtra("videourl", path);
        intent.putExtra("namo", name);
        startActivity(intent);
        finish();
    }
}