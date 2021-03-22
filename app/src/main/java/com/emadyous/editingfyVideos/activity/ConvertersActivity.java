/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.emadyous.editingfyVideos.utils.Helper;
import com.emadyous.editingfyVideos.R;

public class ConvertersActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout toMp3Lay;
    private LinearLayout toGifLay;

    private int witch = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converters);


        toMp3Lay = findViewById(R.id.to_mp3_layout);
        toGifLay = findViewById(R.id.to_gif_layout);


        Button next = findViewById(R.id.next);
        ImageView close = findViewById(R.id.close);

        toMp3Lay.setOnClickListener(this);
        toGifLay.setOnClickListener(this);
        next.setOnClickListener(this);
        close.setOnClickListener(this);


        toMp3Lay.setBackgroundColor(Color.TRANSPARENT);
        toGifLay.setBackgroundResource(R.drawable.choose_tool);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.to_gif_layout) {
            toMp3Lay.setBackgroundColor(Color.TRANSPARENT);
            toGifLay.setBackgroundResource(R.drawable.choose_tool);
            witch = 1;
        } else if (id == R.id.to_mp3_layout) {
            toMp3Lay.setBackgroundResource(R.drawable.choose_tool);
            toGifLay.setBackgroundColor(Color.TRANSPARENT);
            witch = 2;
        }  else if (id == R.id.next) {
            switch (witch) {
                case 2:
                    chooseVideoIntent(3);
                    break;
                case 1:
                    chooseVideoIntent(12);
                    break;
            }
        } else if (id == R.id.close) {
            onBackPressed();
        }
    }


    private void chooseVideoIntent(int id) {
        Helper.ModuleId = id;
        Intent intent = new Intent(this, VideoListActivity.class);
        intent.putExtra("toolId", id);
        startActivity(intent);
    }

}