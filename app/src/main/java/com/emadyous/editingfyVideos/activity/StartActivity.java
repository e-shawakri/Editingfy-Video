/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.emadyous.editingfyVideos.R;
import com.emadyous.editingfyVideos.utils.Helper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;


public class StartActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_start);



        ImageView rate = findViewById(R.id.rate);
        ImageView share = findViewById(R.id.share);

        rate.setOnClickListener(view -> {

            try {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            } catch (ActivityNotFoundException unused2) {
                Toast.makeText(getApplicationContext(), " unable to find market app", Toast.LENGTH_LONG).show();
            }
        });

        share.setOnClickListener(view -> {

            String sb2 = Helper.share_string +
                    "https://play.google.com/store/apps/details?id=" + getPackageName();
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", sb2);
            startActivity(intent);
        });

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (!report.areAllPermissionsGranted()) {
                    Toast.makeText(StartActivity.this, "You need to grant all the permissions to continue using Editingfy Videos.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();

    }

    public void videoConverters(View view) {
        Intent intent = new Intent(StartActivity.this, ConvertersActivity.class);
        startActivity(intent);
    }

    public void videoEdit(View view) {
        Intent intent = new Intent(StartActivity.this, ToolsActivity.class);
        startActivity(intent);
    }

    public void videoMotions(View view) {
        Intent intent = new Intent(StartActivity.this, MotionActivity.class);
        startActivity(intent);
    }


    public void gifs(View view) {
        Intent intent = new Intent(this, GifListActivity.class);
        startActivity(intent);
    }


    public void videos(View view) {
        Intent intent = new Intent(this, VideoListActivity.class);
        intent.putExtra("toolId", 1000);
        startActivity(intent);
    }

}
