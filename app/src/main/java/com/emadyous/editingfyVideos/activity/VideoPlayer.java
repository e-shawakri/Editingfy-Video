/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Video.Media;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.emadyous.editingfyVideos.R;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class VideoPlayer extends AppCompatActivity implements OnSeekBarChangeListener {
    static final boolean r = true;
    Bundle a;
    FloatingActionButton b;
    int c = 0;
    Handler d = new Handler();
    boolean e = false;
    int f = 0;
    SeekBar g;
    Uri h;
    TextView i;
    TextView j;
    TextView k;
    String m = "";
    VideoView n;
    Uri o;
    Runnable p = () -> {
        if (VideoPlayer.this.n.isPlaying()) {
            int currentPosition = VideoPlayer.this.n.getCurrentPosition();
            VideoPlayer.this.g.setProgress(currentPosition);
            try {
                VideoPlayer.this.i.setText(VideoPlayer.formatTimeUnit((long) currentPosition));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (currentPosition == VideoPlayer.this.c) {
                VideoPlayer.this.g.setProgress(0);
                VideoPlayer.this.i.setText("00:00");
                VideoPlayer.this.d.removeCallbacks(VideoPlayer.this.p);
                return;
            }
            VideoPlayer.this.d.postDelayed(VideoPlayer.this.p, 200);
            return;
        }
        VideoPlayer.this.g.setProgress(VideoPlayer.this.c);
        try {
            VideoPlayer.this.i.setText(VideoPlayer.formatTimeUnit((long) VideoPlayer.this.c));
        } catch (ParseException e2) {
            e2.printStackTrace();
        }
        VideoPlayer.this.d.removeCallbacks(VideoPlayer.this.p);
    };
    OnClickListener q = view -> {
        if (VideoPlayer.this.e) {
            try {
                VideoPlayer.this.n.pause();
                VideoPlayer.this.d.removeCallbacks(VideoPlayer.this.p);
                VideoPlayer.this.b.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                VideoPlayer.this.n.seekTo(VideoPlayer.this.g.getProgress());
                VideoPlayer.this.n.start();
                VideoPlayer.this.d.postDelayed(VideoPlayer.this.p, 200);
                VideoPlayer.this.b.setImageResource(R.drawable.ic_baseline_pause_24);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        VideoPlayer.this.e ^= VideoPlayer.r;
    };

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //nada
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //nada
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_video_player);
        this.a = getIntent().getExtras();
        if (this.a != null) {
            this.m = this.a.getString("song");
            this.f = this.a.getInt("position", 0);
        }
        try {
            getVideo(this.m);
        } catch (Exception ignored) {
        }
        k = (TextView) findViewById(R.id.Filename);
        n = (VideoView) findViewById(R.id.videoView);
        g = (SeekBar) findViewById(R.id.sbVideo);
        g.setOnSeekBarChangeListener(this);
        i = (TextView) findViewById(R.id.left_pointer);
        j = (TextView) findViewById(R.id.right_pointer);
        b = findViewById(R.id.btnPlayVideo);
        k.setText(new File(this.m).getName());
        n.setVideoPath(this.m);
        n.seekTo(100);
        n.setOnErrorListener((mediaPlayer, i, i2) -> {
            Toast.makeText(VideoPlayer.this.getApplicationContext(), "Video Player Not Supproting", Toast.LENGTH_SHORT).show();
            return VideoPlayer.r;
        });
        this.n.setOnPreparedListener(mediaPlayer -> {
            c = VideoPlayer.this.n.getDuration();
            j.setText(formatTimeUnit(mediaPlayer.getDuration()));
            g.setMax(VideoPlayer.this.c);
            i.setText("00:00");
        });
        this.n.setOnCompletionListener(mediaPlayer -> {
            b.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            n.seekTo(0);
            g.setProgress(0);
            i.setText("00:00");
            d.removeCallbacks(VideoPlayer.this.p);
            e ^= VideoPlayer.r;
        });
        this.b.setOnClickListener(this.q);

        findViewById(R.id.close).setOnClickListener(view -> onBackPressed());

        findViewById(R.id.share).setOnClickListener(view -> {
            if (n.isPlaying()) {
                n.pause();
                d.removeCallbacks(p);
                VideoPlayer.this.b.setImageResource(R.drawable.ic_baseline_pause_24);
                e = false;
            }
            try {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("video/*");
                intent.putExtra("android.intent.extra.STREAM", h);
                startActivity(Intent.createChooser(intent, "Share File"));
            } catch (Exception ignored) {
            }
        });

        findViewById(R.id.delete).setOnClickListener(view -> {
            if (n.isPlaying()) {
                try {
                    n.pause();
                    d.removeCallbacks(p);
                    b.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    e = false;
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            delete();
        });

    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
        if (z) {
            this.n.seekTo(i2);
            try {
                this.i.setText(formatTimeUnit((long) i2));
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
        }
    }

    @SuppressLint({"NewApi", "DefaultLocale"})
    public static String formatTimeUnit(long j2) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(j2), TimeUnit.MILLISECONDS.toSeconds(j2) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j2)));
    }

    public void getVideo(String str) {
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        String[] strArr = {"_id", "_data", "_display_name", "_size", "duration", "date_added", "album"};
        String sb = "%" +
                str +
                "%";
        String[] strArr2 = {sb};
        Cursor managedQuery = managedQuery(uri, strArr, "_data  like ?", strArr2, " _id DESC");
        if (managedQuery.moveToFirst()) {
            try {
                Uri withAppendedPath = Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, getLong(managedQuery));
                this.o = withAppendedPath;
                this.h = withAppendedPath;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static String getLong(Cursor cursor) {
        return String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
    }

    public void delete() {
        new AlertDialog.Builder(this).setMessage("Are you sure you want to delete this file ?").setPositiveButton("delete", (dialogInterface, i) -> {
            File file = new File(VideoPlayer.this.m);
            if (file.exists()) {
                file.delete();
                try {
                    ContentResolver contentResolver = VideoPlayer.this.getContentResolver();
                    Uri uri = VideoPlayer.this.o;
                    String sb = "_data=\"" +
                            VideoPlayer.this.m +
                            "\"";
                    contentResolver.delete(uri, sb, null);
                } catch (Exception ignored) {
                }
            }
            VideoPlayer.this.onBackPressed();
        }).setNegativeButton("Cancel", (dialogInterface, i) -> {
        }).setCancelable(r).show();
    }

}
