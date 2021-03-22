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

public class ToolsActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout crop;
    private LinearLayout cut;
    private LinearLayout mute;
    private LinearLayout rotate;
    private LinearLayout mirror;
    private LinearLayout reverse;

    private int witch = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);


        crop = findViewById(R.id.crop_layout);
        cut = findViewById(R.id.cut_layout);
        mute = findViewById(R.id.mute_layout);
        rotate = findViewById(R.id.rotate_layout);
        mirror = findViewById(R.id.mirror_layout);
        reverse = findViewById(R.id.reverse_layout);

        Button next = findViewById(R.id.next);
        ImageView close = findViewById(R.id.close);

        crop.setOnClickListener(this);
        cut.setOnClickListener(this);
        mute.setOnClickListener(this);
        rotate.setOnClickListener(this);
        mirror.setOnClickListener(this);
        reverse.setOnClickListener(this);

        next.setOnClickListener(this);
        close.setOnClickListener(this);


        cut.setBackgroundResource(R.drawable.choose_tool);
        crop.setBackgroundColor(Color.TRANSPARENT);
        mute.setBackgroundColor(Color.TRANSPARENT);
        rotate.setBackgroundColor(Color.TRANSPARENT);
        mirror.setBackgroundColor(Color.TRANSPARENT);
        reverse.setBackgroundColor(Color.TRANSPARENT);

    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cut_layout) {
            cut.setBackgroundResource(R.drawable.choose_tool);
            crop.setBackgroundColor(Color.TRANSPARENT);
            mute.setBackgroundColor(Color.TRANSPARENT);
            rotate.setBackgroundColor(Color.TRANSPARENT);
            mirror.setBackgroundColor(Color.TRANSPARENT);
            reverse.setBackgroundColor(Color.TRANSPARENT);
            witch = 0;
        } else if (id == R.id.crop_layout) {
            crop.setBackgroundResource(R.drawable.choose_tool);
            cut.setBackgroundColor(Color.TRANSPARENT);
            mute.setBackgroundColor(Color.TRANSPARENT);
            rotate.setBackgroundColor(Color.TRANSPARENT);
            mirror.setBackgroundColor(Color.TRANSPARENT);
            reverse.setBackgroundColor(Color.TRANSPARENT);
            witch = 1;
        } else if (id == R.id.mute_layout) {
            mute.setBackgroundResource(R.drawable.choose_tool);
            crop.setBackgroundColor(Color.TRANSPARENT);
            cut.setBackgroundColor(Color.TRANSPARENT);
            rotate.setBackgroundColor(Color.TRANSPARENT);
            mirror.setBackgroundColor(Color.TRANSPARENT);
            reverse.setBackgroundColor(Color.TRANSPARENT);
            witch = 2;
        } else if (id == R.id.rotate_layout) {
            rotate.setBackgroundResource(R.drawable.choose_tool);
            crop.setBackgroundColor(Color.TRANSPARENT);
            mute.setBackgroundColor(Color.TRANSPARENT);
            cut.setBackgroundColor(Color.TRANSPARENT);
            mirror.setBackgroundColor(Color.TRANSPARENT);
            reverse.setBackgroundColor(Color.TRANSPARENT);
            witch = 3;
        } else if (id == R.id.mirror_layout) {
            mirror.setBackgroundResource(R.drawable.choose_tool);
            crop.setBackgroundColor(Color.TRANSPARENT);
            mute.setBackgroundColor(Color.TRANSPARENT);
            rotate.setBackgroundColor(Color.TRANSPARENT);
            cut.setBackgroundColor(Color.TRANSPARENT);
            reverse.setBackgroundColor(Color.TRANSPARENT);
            witch = 4;
        } else if (id == R.id.reverse_layout) {
            reverse.setBackgroundResource(R.drawable.choose_tool);
            crop.setBackgroundColor(Color.TRANSPARENT);
            mute.setBackgroundColor(Color.TRANSPARENT);
            rotate.setBackgroundColor(Color.TRANSPARENT);
            mirror.setBackgroundColor(Color.TRANSPARENT);
            cut.setBackgroundColor(Color.TRANSPARENT);
            witch = 5;
        } else if (id == R.id.next) {
            switch (witch) {
                case 0:
                    chooseVideoIntent(1);
                    break;
                case 1:
                    chooseVideoIntent(11);
                    break;
                case 2:
                    chooseVideoIntent(2);

                    break;
                case 3:
                    chooseVideoIntent(13);
                    break;
                case 4:

                    chooseVideoIntent(14);
                    break;
                case 5:

                    chooseVideoIntent(16);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + witch);
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