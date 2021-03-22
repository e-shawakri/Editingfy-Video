/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.model;

public class AudioData {

    private String id;
    private String artist;
    private String title;
    private String data;
    private String displayName;
    private String duration;

    public AudioData(String id, String artist, String title, String data, String displayName, String duration) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.data = data;
        this.displayName = displayName;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
