/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.emadyous.editingfyVideos.R;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class AudioPlayer extends AppCompatActivity implements OnSeekBarChangeListener {
    static final boolean BOOLEN = true;
    Bundle a;
    ImageView b;
    FloatingActionButton c;
    int d = 0;
    Handler e = new Handler();
    boolean f = false;
    boolean g = false;
    SeekBar h;
    Uri i;
    TextView j;
    TextView k;
    TextView l;
    String n = "";
    Runnable o = new Runnable() {
        public void run() {
            if (AudioPlayer.this.r.isPlaying()) {
                int currentPosition = AudioPlayer.this.r.getCurrentPosition();
                AudioPlayer.this.h.setProgress(currentPosition);
                AudioPlayer.this.j.setText(AudioPlayer.formatTimeUnit((long) currentPosition));
                if (currentPosition == AudioPlayer.this.d) {
                    AudioPlayer.this.h.setProgress(0);
                    AudioPlayer.this.j.setText(zero);
                    AudioPlayer.this.e.removeCallbacks(AudioPlayer.this.o);
                    return;
                }
                AudioPlayer.this.e.postDelayed(AudioPlayer.this.o, 200);
                return;
            }
            AudioPlayer.this.h.setProgress(AudioPlayer.this.d);
            AudioPlayer.this.j.setText(AudioPlayer.formatTimeUnit((long) AudioPlayer.this.d));
            AudioPlayer.this.e.removeCallbacks(AudioPlayer.this.o);
        }
    };
    OnClickListener p = new OnClickListener() {
        @Override
        public void onClick(View view) {
            String sb = "play status " +
                    AudioPlayer.this.f;
            Log.e("", sb);
            if (AudioPlayer.this.f) {
                try {
                    r.pause();
                    e.removeCallbacks(AudioPlayer.this.o);
                    c.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                } catch (IllegalStateException e1) {
                    e1.printStackTrace();
                }
            } else {
                try {
                    r.seekTo(AudioPlayer.this.h.getProgress());
                    r.start();
                    e.postDelayed(AudioPlayer.this.o, 200);
                    c.setImageResource(R.drawable.ic_baseline_pause_24);
                } catch (IllegalStateException e2) {
                    e2.printStackTrace();
                }
            }
            AudioPlayer.this.f ^= AudioPlayer.BOOLEN;
        }
    };

    MediaPlayer r;
    public static final String zero = "00:00";

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //add
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //add
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_audio_player);
        a = getIntent().getExtras();
        String artistName = a.getString("artist");
        n = a.getString("song");
        g = a.getBoolean("isfrom", false);
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(this.n)));
        sendBroadcast(intent);
        l = findViewById(R.id.Filename);
        l.setText(new File(this.n).getName());


        b = findViewById(R.id.artwork);
        Glide.with(this)
                .load(getAlbumImage(n))
                .placeholder(R.drawable.audio_holder)
                .into(b);


        h = findViewById(R.id.sbVideo);
        h.setOnSeekBarChangeListener(this);
        r = MediaPlayer.create(this, Uri.parse(this.n));
        j = findViewById(R.id.tvStartVideo);
        k = findViewById(R.id.tvEndVideo);
        c = findViewById(R.id.btnPlayVideo);

        TextView artist = findViewById(R.id.artist);
        artist.setText(artistName);

        findViewById(R.id.close).setOnClickListener(view -> onBackPressed());

        findViewById(R.id.share).setOnClickListener(view -> {
            try {
                Intent intent1 = new Intent("android.intent.action.SEND");
                intent1.setType("audio/*");
                intent1.putExtra("android.intent.extra.STREAM", i);
                startActivity(Intent.createChooser(intent1, "Share File"));
            } catch (Exception unused) {
                //add
            }
        });

        r.setOnErrorListener((mediaPlayer, i, i2) -> {
            Toast.makeText(AudioPlayer.this.getApplicationContext(), "Audio Player Not Supproting", Toast.LENGTH_LONG).show();
            return AudioPlayer.BOOLEN;
        });
        this.r.setOnPreparedListener(mediaPlayer -> {
            AudioPlayer.this.d = AudioPlayer.this.r.getDuration();
            AudioPlayer.this.h.setMax(AudioPlayer.this.d);
            AudioPlayer.this.j.setText(zero);
            AudioPlayer.this.k.setText(AudioPlayer.formatTimeUnit(AudioPlayer.this.d));
        });
        this.r.setOnCompletionListener(mediaPlayer -> {
            c.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            AudioPlayer.this.r.seekTo(0);
            AudioPlayer.this.h.setProgress(0);
            AudioPlayer.this.j.setText(zero);
            AudioPlayer.this.e.removeCallbacks(AudioPlayer.this.o);
            AudioPlayer.this.f ^= AudioPlayer.BOOLEN;
        });
        this.c.setOnClickListener(this.p);

    }

    private Bitmap getAlbumImage(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        byte[] data = mmr.getEmbeddedPicture();
        if (data != null) return BitmapFactory.decodeByteArray(data, 0, data.length);
        return null;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
        if (z) {
            this.r.seekTo(i2);
            this.j.setText(formatTimeUnit((long) i2));
        }
    }

    @SuppressLint({"NewApi", "DefaultLocale"})
    public static String formatTimeUnit(long j2) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(j2), TimeUnit.MILLISECONDS.toSeconds(j2) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j2)));
    }


    public String getLong(Cursor cursor) {
        return String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
    }

    @Override
    protected void onDestroy() {
        e.removeCallbacks(o);
        if (this.r.isPlaying()) {
            this.r.stop();
            r.release();
        }
        super.onDestroy();
    }
}
