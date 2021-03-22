/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.emadyous.editingfyVideos.R;
import com.emadyous.editingfyVideos.databinding.AudioFileItemBinding;
import com.emadyous.editingfyVideos.model.AudioData;
import com.bumptech.glide.Glide;

import java.util.List;

public class AudioFilesAdapter extends RecyclerView.Adapter<AudioFilesAdapter.ViewHolder> {

    private final List<AudioData> list;
    private final VideoFilesHandler handler;

    public AudioFilesAdapter(List<AudioData> list, VideoFilesHandler handler) {
        this.list = list;
        this.handler = handler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AudioFileItemBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.audio_file_item, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageView image = holder.mBinding.audioPreview;

        holder.mBinding.setAudio(list.get(position));

        Glide.with(image)
                .load(R.drawable.audio_holder)
                .placeholder(R.drawable.audio_holder)
                .into(image);

        Log.d("TAG", "onBindViewHolder: " + list.get(position).getData());

        holder.mBinding.getRoot().setOnClickListener(view -> handler.onChoose(position, list.get(position).getData(), list.get(position).getArtist()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private Bitmap getAlbumImage(String path) {
        byte[] data;

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        data = mmr.getEmbeddedPicture();
        mmr.close();

        if (data != null) return BitmapFactory.decodeByteArray(data, 0, data.length);
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final AudioFileItemBinding mBinding;

        public ViewHolder(@NonNull AudioFileItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
    }

    public interface VideoFilesHandler {
        void onChoose(int position, String path, String artist);
    }
}
