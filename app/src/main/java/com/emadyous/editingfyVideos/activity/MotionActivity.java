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

public class MotionActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout slow;
    private LinearLayout fast;

    private int witch = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);



        fast = findViewById(R.id.fast_layout);
        slow = findViewById(R.id.slow_layout);

        Button next = findViewById(R.id.next);
        ImageView close = findViewById(R.id.close);

        fast.setOnClickListener(this);
        slow.setOnClickListener(this);
        next.setOnClickListener(this);
        close.setOnClickListener(this);

        fast.setBackgroundResource(R.drawable.choose_tool);
        slow.setBackgroundColor(Color.TRANSPARENT);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.slow_layout) {
            slow.setBackgroundResource(R.drawable.choose_tool);
            fast.setBackgroundColor(Color.TRANSPARENT);
            witch = 1;
        } else if (id == R.id.fast_layout) {
            fast.setBackgroundResource(R.drawable.choose_tool);
            slow.setBackgroundColor(Color.TRANSPARENT);
            witch = 0;
        } else if (id == R.id.next) {
            switch (witch) {
                case 0:
                    chooseVideoIntent(10);
                    break;
                case 1:
                    chooseVideoIntent(9);
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