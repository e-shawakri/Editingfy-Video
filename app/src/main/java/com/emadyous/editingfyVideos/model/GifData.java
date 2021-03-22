/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.model;

import android.net.Uri;

public class GifData {
    public String duration;
    public Uri gifUri;
    public long videoId;
    public String videoName;
    public String videoPath;

    public GifData(String duration, Uri gifUri, String videoName, String videoPath) {
        this.duration = duration;
        this.gifUri = gifUri;
        this.videoName = videoName;
        this.videoPath = videoPath;
    }

    public GifData(String str, Uri uri, String str2) {
        this.videoName = str;
        this.gifUri = uri;
        this.videoPath = str2;
    }
}
