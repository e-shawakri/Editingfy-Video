/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.emadyous.editingfyVideos.R;
import com.emadyous.editingfyVideos.databinding.VideoFileItemBinding;
import com.emadyous.editingfyVideos.model.VideoData;
import com.bumptech.glide.Glide;

import java.util.List;

public class VideoFilesAdapter extends RecyclerView.Adapter<VideoFilesAdapter.ViewHolder> {

    private final List<VideoData> list;
    private final VideoFilesHandler handler;

    public VideoFilesAdapter(List<VideoData> list, VideoFilesHandler handler) {
        this.list = list;
        this.handler = handler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VideoFileItemBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.video_file_item, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageView image = holder.mBinding.videoPreview;

        Glide.with(image)
                .load(list.get(position).videouri)
                .into(image);

        holder.mBinding.getRoot().setOnClickListener(view -> handler.onChoose(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final VideoFileItemBinding mBinding;

        public ViewHolder(@NonNull VideoFileItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
    }

    public interface VideoFilesHandler {
        void onChoose(int position);
    }
}
