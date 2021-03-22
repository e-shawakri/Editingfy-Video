/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Media;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.Statistics;
import com.arthenica.mobileffmpeg.StatisticsCallback;
import com.emadyous.editingfyVideos.R;
import com.emadyous.editingfyVideos.ui.VideoPlayerState;
import com.emadyous.editingfyVideos.utils.VideoSliceSeekBar;
import com.emadyous.editingfyVideos.utils.VideoSliceSeekBar.SeekBarChangeListener;
import com.emadyous.editingfyVideos.utils.FileUtils;
import com.edmodo.cropper.CropImageView;
import com.edmodo.cropper.cropwindow.edge.Edge;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static java.lang.String.format;
import static java.lang.String.valueOf;

public class VideoCropActivity extends AppCompatActivity {
    TextView rotaa;
    static final boolean af = true;
    int A;
    int B;
    String C;
    String D;
    String E;
    String F;
    String G = "00";
    String H;
    TextView S;
    TextView T;
    Bitmap U;
    FloatingActionButton V;
    VideoPlayerState W = new VideoPlayerState();
    VideoSliceSeekBar X;
    VideoView Z;
    CropImageView a;
    float aa;
    float ab;
    float ac;
    float ad;
    long ae;
    int ag;
    int ah;
    String b;
    ImageView c;
    ImageView d;
    ImageView h;
    ImageView j;
    String l;
    int m;
    int n;
    int o;
    int p;
    int q;
    int r;
    int s;
    int t;
    int u;
    int v;
    int w;
    int x;
    int y;
    int z;
    private long executionId;
    int videoLength;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_crop);


        this.E = getIntent().getStringExtra("videofilename");
        if (this.E != null) {
            this.U = ThumbnailUtils.createVideoThumbnail(this.E, 1);
        }

        this.x = FileUtils.getScreenWidth();
        this.a = findViewById(R.id.cropperView);
        d();
        e();
       VideoCropActivity.this.c();

    }


    public void b() {
            c();

    }


    public void c() {
        Intent intent = new Intent(getApplicationContext(), VideoPlayer.class);
        intent.putExtra("song", this.D);
        startActivity(intent);
        finish();
    }

    public void cropcommand() {
        h();
        getPath();
    }

    @SuppressLint("InvalidWakeLockTag")
    public void getPath() {
        if (this.w == 90) {
            try {
                this.o = this.B;
                int i2 = this.z;
                u = this.B;
                v = this.A;
                m = this.y;
                n = this.z;
                s = this.y;
                t = this.A;
                ag = this.m - this.o;
                ah = this.v - i2;
                p = this.q - (this.ah + i2);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else if (this.w == 270) {
            try {
                int i3 = this.B;
                int i4 = this.z;
                u = this.B;
                v = this.A;
                m = this.y;
                n = this.z;
                s = this.y;
                t = this.A;
                ag = this.m - i3;
                ah = this.v - i4;
                o = this.r - (this.ag + i3);
                p = i4;
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } else {
            try {
                o = this.z;
                p = this.B;
                u = this.A;
                v = this.B;
                m = this.z;
                n = this.y;
                s = this.A;
                t = this.y;
                ag = this.u - this.o;
                ah = this.n - this.p;
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
        this.H = String.valueOf(this.W.getStart() / 1000);
        this.F = String.valueOf(this.W.getDuration() / 1000);
        this.l = this.E;
        if (this.l.contains(".3gp") || this.l.contains(".3GP")) {
            try {
                this.C = FileUtils.getTargetFileName(this, this.l.replace(".3gp", ".mp4"));
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        } else if (this.l.contains(".flv") || this.l.contains(".FLv")) {
            try {
                this.C = FileUtils.getTargetFileName(this, this.l.replace(".flv", ".mp4"));
            } catch (Exception e6) {
                e6.printStackTrace();
            }
        } else if (this.l.contains(".mov") || this.l.contains(".MOV")) {
            try {
                this.C = FileUtils.getTargetFileName(this, this.l.replace(".mov", ".mp4"));
            } catch (Exception e7) {
                e7.printStackTrace();
            }
        } else if (this.l.contains(".wmv") || this.l.contains(".WMV")) {
            try {
                this.C = FileUtils.getTargetFileName(this, this.l.replace(".wmv", ".mp4"));
            } catch (Exception e8) {
                e8.printStackTrace();
            }
        } else {
            try {
                this.C = FileUtils.getTargetFileName(this, this.l);
            } catch (Exception e9) {
                e9.printStackTrace();
            }
        }
        this.D = FileUtils.getTargetFileName(this, this.C);
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long availableBlocks = ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
        File file = new File(this.W.getFilename());
        Log.d("TAG", "getpath: " + file.getAbsolutePath());
        this.ae = 0;
        this.ae = file.length() / 1024;
        if ((availableBlocks / 1024) / 1024 >= this.ae / 1024) {
            try {
                String sb = "crop=w=" +
                        this.ag +
                        ":h=" +
                        this.ah +
                        ":x=" +
                        this.o +
                        ":y=" +
                        this.p;


                 a(new String[]{"-y", "-ss", H, "-t", F, "-i", l, "-strict", "experimental", "-filter_complex", sb, "-r", "15", "-ab", "128k", "-vcodec", "mpeg4", "-acodec", "copy", "-b:v", "2500k", "-sample_fmt", "s16", "-ss", "0", "-t", this.F, this.D}, this.D);


            } catch (Exception unused) {
                File file2 = new File(this.D);
                if (file2.exists()) {
                    file2.delete();
                    finish();
                    return;
                }
                Toast.makeText(this, "please select any option!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Out of Memory!......", Toast.LENGTH_SHORT).show();
        }
    }


    private void a(String[] comand, final String str) {

        videoLength = Integer.parseInt(F) * 1000;

        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_progress);

        Button cancel = dialog.findViewById(R.id.cancel);
        ProgressBar progBar = dialog.findViewById(R.id.progress_bar);
        progBar.setProgress(0);
//        TextView progText = (TextView) dialog.findViewById(R.id.progress_text_bar);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.arthenica.mobileffmpeg.FFmpeg.cancel(executionId);
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        executionId = FFmpeg.executeAsync(comand, new ExecuteCallback() {
            @Override
            public void apply(long executionId, int returnCode) {
                if (returnCode == RETURN_CODE_SUCCESS) {
                    addVideo(str);
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(VideoCropActivity.this, "Execution completed successfully.", Toast.LENGTH_SHORT).show();
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(VideoCropActivity.this, "Execution cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    progBar.setProgress(0);
                    dialog.dismiss();
                    Toast.makeText(VideoCropActivity.this, "Execution failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        com.arthenica.mobileffmpeg.Config.enableStatisticsCallback(new StatisticsCallback() {
            public void apply(Statistics newStatistics) {

                float progress = Float.parseFloat(valueOf(newStatistics.getTime())) / videoLength;
                float progressFinal = progress * 100;
                Log.d(com.arthenica.mobileffmpeg.Config.TAG, "Video Length: " + progressFinal);
                Log.d(com.arthenica.mobileffmpeg.Config.TAG, format("frame: %d, time: %d", newStatistics.getVideoFrameNumber(), newStatistics.getTime()));
                Log.d(com.arthenica.mobileffmpeg.Config.TAG, format("Quality: %f, time: %f", newStatistics.getVideoQuality(), newStatistics.getVideoFps()));
                progBar.setProgress((int) progressFinal);
//                progText.setText(format("%d%%", (int) progressFinal));

                String frame = "Frame: " + newStatistics.getVideoFrameNumber();
                String fps = "Fps: " + newStatistics.getVideoFps();
                String quality = "Quality: " + newStatistics.getVideoQuality();
                String time = "Time: " + simpleDateFormat.format(newStatistics.getTime());
                String bitrate = "Bitrate: " + newStatistics.getBitrate();
                String speed = "Speed: " + newStatistics.getSpeed();
                String size = "Size: " + Formatter.formatFileSize(getApplicationContext(), newStatistics.getSize());
            }
        });

    }

    public Uri addVideo(String videoFile) {
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Video.Media.TITLE, "Crop_" + System.currentTimeMillis());
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Video.Media.DURATION, videoLength);
        }
        values.put(MediaStore.Video.Media.DESCRIPTION, "Cropped Via Editingfy Video");
        values.put(MediaStore.Video.Media.TAGS, "Editingfy Video");
        values.put(MediaStore.Video.Media.DATA, videoFile);
        return getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

    @Override
    public void onResume() {
        this.Z.seekTo(this.W.getCurrentTime());
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        this.W.setCurrentTime(this.Z.getCurrentPosition());
    }

    private void d() {
        this.rotaa = (TextView) findViewById(R.id.left_pointer);
        this.S = (TextView) findViewById(R.id.right_pointer);
        this.X = (VideoSliceSeekBar) findViewById(R.id.seek_bar);
        this.T = (TextView) findViewById(R.id.Filename);
        this.T.setText(new File(this.E).getName());
        this.V = findViewById(R.id.buttonply);

        ImageView save = findViewById(R.id.save);
        save.setOnClickListener(view -> {
            if (Z != null && Z.isPlaying()) {
                Z.pause();
            }
            cropcommand();
        });

        ImageView close = findViewById(R.id.close);
        close.setOnClickListener(view -> onBackPressed());

        this.V.setOnClickListener(view -> {
            if (VideoCropActivity.this.Z == null || !VideoCropActivity.this.Z.isPlaying()) {
                VideoCropActivity.this.V.setImageResource(R.drawable.ic_baseline_pause_24);
            } else {
                VideoCropActivity.this.V.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            }
            VideoCropActivity.this.g();
        });
        Object lastNonConfigurationInstance = getLastNonConfigurationInstance();
        if (lastNonConfigurationInstance != null) {
            this.W = (VideoPlayerState) lastNonConfigurationInstance;
        } else {
            this.W.setFilename(this.E);
        }
        this.c = findViewById(R.id.imbtn_custom);
        this.c.setOnClickListener(setRatioOriginal());
        this.j = findViewById(R.id.imgbtn_square);
        this.j.setOnClickListener(setRatioSqaure());
        this.h = findViewById(R.id.imgbtn_port);
        this.h.setOnClickListener(setRatioPort());
        this.d = findViewById(R.id.imgbtn_cland);
        this.d.setOnClickListener(setRatioLand());
    }

    @SuppressLint({"NewApi"})
    private void e() {
        this.Z = (VideoView) findViewById(R.id.videoview);
        this.Z.setVideoPath(this.E);
        this.b = getTimeForTrackFormat(this.Z.getDuration(), af);

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this.E);
        this.r = Integer.parseInt(mediaMetadataRetriever.extractMetadata(18));
        this.q = Integer.parseInt(mediaMetadataRetriever.extractMetadata(19));
        this.w = Integer.parseInt(mediaMetadataRetriever.extractMetadata(24));
        mediaMetadataRetriever.release();

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.a.getLayoutParams();
        if (this.w == 90 || this.w == 270) {
            if (this.r >= this.q) {
                if (this.r >= this.x) {
                    layoutParams.height = this.x;
                    layoutParams.width = (int) (((float) this.x) / (((float) this.r) / ((float) this.q)));
                } else {
                    layoutParams.width = this.x;
                    layoutParams.height = (int) (((float) this.q) * (((float) this.x) / ((float) this.r)));
                }
            } else if (this.q >= this.x) {
                layoutParams.width = this.x;
                layoutParams.height = (int) (((float) this.x) / (((float) this.q) / ((float) this.r)));
            } else {
                layoutParams.width = (int) (((float) this.r) * (((float) this.x) / ((float) this.q)));
                layoutParams.height = this.x;
            }
        } else if (this.r >= this.q) {
            if (this.r >= this.x) {
                layoutParams.width = this.x;
                layoutParams.height = (int) (((float) this.x) / (((float) this.r) / ((float) this.q)));
            } else {
                layoutParams.width = this.x;
                layoutParams.height = (int) (((float) this.q) * (((float) this.x) / ((float) this.r)));
            }
        } else if (this.q >= this.x) {
            layoutParams.width = (int) (((float) this.x) / (((float) this.q) / ((float) this.r)));
            layoutParams.height = this.x;
        } else {
            layoutParams.width = (int) (((float) this.r) * (((float) this.x) / ((float) this.q)));
            layoutParams.height = this.x;
        }
        this.a.setLayoutParams(layoutParams);
        this.a.setImageBitmap(Bitmap.createBitmap(layoutParams.width, layoutParams.height, Config.ARGB_8888));
        try {
            searchVideo(this.E);
        } catch (Exception ignored) {
        }
        this.Z.setOnPreparedListener(mediaPlayer -> {
            VideoCropActivity.this.X.setSeekBarChangeListener(new SeekBarChangeListener() {
                public void SeekBarValueChanged(int i, int i2) {
                    if (VideoCropActivity.this.X.getSelectedThumb() == 1) {
                        VideoCropActivity.this.Z.seekTo(VideoCropActivity.this.X.getLeftProgress());
                    }
                    VideoCropActivity.this.rotaa.setText(VideoCropActivity.getTimeForTrackFormat(i, VideoCropActivity.af));
                    VideoCropActivity.this.S.setText(VideoCropActivity.getTimeForTrackFormat(i2, VideoCropActivity.af));
                    VideoCropActivity.this.G = VideoCropActivity.getTimeForTrackFormat(i, VideoCropActivity.af);
                    VideoCropActivity.this.W.setStart(i);
                    VideoCropActivity.this.b = VideoCropActivity.getTimeForTrackFormat(i2, VideoCropActivity.af);
                    VideoCropActivity.this.W.setStop(i2);
                }
            });
            VideoCropActivity.this.b = VideoCropActivity.getTimeForTrackFormat(mediaPlayer.getDuration(), VideoCropActivity.af);
            VideoCropActivity.this.X.setMaxValue(mediaPlayer.getDuration());
            VideoCropActivity.this.X.setLeftProgress(0);
            VideoCropActivity.this.X.setRightProgress(mediaPlayer.getDuration());
            VideoCropActivity.this.X.setProgressMinDiff(0);
            VideoCropActivity.this.Z.seekTo(100);
        });
    }

    public void g() {
        if (this.Z.isPlaying()) {
            this.Z.pause();
            this.X.setSliceBlocked(false);
            this.X.removeVideoStatusThumb();
            return;
        }
        this.Z.seekTo(this.X.getLeftProgress());
        this.Z.start();
        this.X.videoPlayingProgress(this.X.getLeftProgress());
    }

    public static String getTimeForTrackFormat(int i2, boolean z2) {
        String str;
        int i3 = i2 / 60000;
        int i4 = (i2 - ((i3 * 60) * 1000)) / 1000;
        StringBuilder sb = new StringBuilder(String.valueOf((!z2 || i3 >= 10) ? "" : "0"));
        sb.append(i3 % 60);
        sb.append(":");
        String sb2 = sb.toString();
        if (i4 < 10) {
            String sb3 = String.valueOf(sb2) + "0" +
                    i4;
            str = sb3;
        } else {
            str = String.valueOf(sb2) + i4;
        }
        String sb5 = "Display Result" +
                str;
        Log.e("", sb5);
        return str;
    }

    private void h() {
        if (this.w == 90 || this.w == 270) {
            this.aa = (float) this.q;
            this.ab = (float) this.r;
            this.ac = (float) this.a.getWidth();
            this.ad = (float) this.a.getHeight();
            this.z = (int) ((Edge.LEFT.getCoordinate() * this.aa) / this.ac);
            this.A = (int) ((Edge.RIGHT.getCoordinate() * this.aa) / this.ac);
            this.B = (int) ((Edge.TOP.getCoordinate() * this.ab) / this.ad);
            this.y = (int) ((Edge.BOTTOM.getCoordinate() * this.ab) / this.ad);
            return;
        }
        this.aa = (float) this.r;
        this.ab = (float) this.q;
        this.ac = (float) this.a.getWidth();
        this.ad = (float) this.a.getHeight();
        this.z = (int) ((Edge.LEFT.getCoordinate() * this.aa) / this.ac);
        this.A = (int) ((Edge.RIGHT.getCoordinate() * this.aa) / this.ac);
        this.B = (int) ((Edge.TOP.getCoordinate() * this.ab) / this.ad);
        this.y = (int) ((Edge.BOTTOM.getCoordinate() * this.ab) / this.ad);
    }

    public OnClickListener setRatioOriginal() {
        return view -> {
            VideoCropActivity.this.a.setFixedAspectRatio(false);
            VideoCropActivity.this.c.setBackgroundResource(R.drawable.ic_crop_custom_press);
            j.setColorFilter(ContextCompat.getColor(VideoCropActivity.this, R.color.colorPrimary), PorterDuff.Mode.OVERLAY);
            h.setColorFilter(ContextCompat.getColor(VideoCropActivity.this, R.color.colorPrimary), PorterDuff.Mode.OVERLAY);
            d.setColorFilter(ContextCompat.getColor(VideoCropActivity.this, R.color.colorPrimary), PorterDuff.Mode.OVERLAY);

        };
    }

    public OnClickListener setRatioSqaure() {
        return view -> {
            VideoCropActivity.this.a.setFixedAspectRatio(VideoCropActivity.af);
            VideoCropActivity.this.a.setAspectRatio(10, 10);
            VideoCropActivity.this.j.setBackgroundResource(R.drawable.ic_crop_square_press);

            c.setColorFilter(ContextCompat.getColor(VideoCropActivity.this, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            h.setColorFilter(ContextCompat.getColor(VideoCropActivity.this, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            d.setColorFilter(ContextCompat.getColor(VideoCropActivity.this, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        };
    }

    public OnClickListener setRatioPort() {
        return view -> {
            VideoCropActivity.this.a.setFixedAspectRatio(VideoCropActivity.af);
            VideoCropActivity.this.a.setAspectRatio(8, 16);
            VideoCropActivity.this.h.setBackgroundResource(R.drawable.ic_crop_portrait_press);

        };
    }

    public OnClickListener setRatioLand() {
        return view -> {
            VideoCropActivity.this.a.setFixedAspectRatio(VideoCropActivity.af);
            VideoCropActivity.this.a.setAspectRatio(16, 8);
            VideoCropActivity.this.d.setBackgroundResource(R.drawable.ic_crop_landscape_press);

        };
    }


    public void searchVideo(String str) {
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        String[] strArr = {"_data", "_id"};
        String sb = "%" +
                str +
                "%";
        Cursor managedQuery = managedQuery(uri, strArr, "_data  like ?", new String[]{sb}, " _id DESC");
        int count = managedQuery.getCount();
        String sb2 = "count" +
                count;
        Log.e("", sb2);
        if (count > 0) {
            managedQuery.moveToFirst();
            managedQuery.getLong(managedQuery.getColumnIndexOrThrow("_id"));
            managedQuery.moveToNext();
        }
    }

    public void j() {
        new AlertDialog.Builder(this).setTitle("Device not supported").setMessage("Editingfy is not supported on your device").setCancelable(false).setPositiveButton("Ok", (dialogInterface, i) -> VideoCropActivity.this.finish()).create().show();
    }

    public void deleteFromGallery(String str) {
        String[] strArr = {"_id"};
        String[] strArr2 = {str};
        Uri uri = Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor query = contentResolver.query(uri, strArr, "_data = ?", strArr2, null);
        if (query.moveToFirst()) {
            try {
                contentResolver.delete(ContentUris.withAppendedId(Images.Media.EXTERNAL_CONTENT_URI, query.getLong(query.getColumnIndexOrThrow("_id"))), null, null);
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            }
        } else {
            try {
                boolean delete = new File(str).delete();
                if (delete) {
                    refreshGallery(str);
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        query.close();
    }

    public void refreshGallery(String str) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(str)));
        sendBroadcast(intent);
    }

}
