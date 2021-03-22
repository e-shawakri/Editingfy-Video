/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.emadyous.editingfyVideos.R;
import com.bumptech.glide.Glide;

import java.io.File;


public class GIFPreviewActivity extends AppCompatActivity {

    ImageView view;
    ProgressDialog e;
    TextView f;
    private String i;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_gif_player);


        Intent intent = getIntent();
        i = intent.getStringExtra("videourl");
        view = (ImageView) findViewById(R.id.imgGif);
        Glide.with(this)
                .load(i)
                .into(view);

        f = (TextView) findViewById(R.id.Filename);
//        f.setText(i.substring(i.lastIndexOf("/") + 1, i.lastIndexOf(".")));
//        f.setText(i);


        findViewById(R.id.close).setOnClickListener(view -> onBackPressed());

        findViewById(R.id.delete).setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(GIFPreviewActivity.this);
            builder.setMessage("Are you sure, You want to delete this file?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                File file = new File(GIFPreviewActivity.this.i);
                if (file.exists()) {
                    file.delete();
                }
                ContentResolver contentResolver = GIFPreviewActivity.this.getContentResolver();
                Uri uri = Media.EXTERNAL_CONTENT_URI;
                String sb = "_data='" +
                        GIFPreviewActivity.this.i +
                        "'";
                contentResolver.delete(uri, sb, null);
                e = new ProgressDialog(GIFPreviewActivity.this);
                e.setCancelable(false);
                e.setMessage(Html.fromHtml("<font color='#f45677'> Please wait... </font>"));
                e.show();
            });
            builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
        });

        findViewById(R.id.share).setOnClickListener(view -> {
            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.putExtra("android.intent.extra.SUBJECT", "Video Maker");
            intent2.setType("video/*");
            intent2.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(i)));
            intent2.putExtra("android.intent.extra.TEXT", "video");
            startActivity(Intent.createChooser(intent2, "Where to Share?"));
        });

    }

}
