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
import com.emadyous.editingfyVideos.databinding.GifFileItemBinding;
import com.emadyous.editingfyVideos.model.GifData;
import com.bumptech.glide.Glide;

import java.util.List;

public class GifsFilesAdapter extends RecyclerView.Adapter<GifsFilesAdapter.ViewHolder> {

    private final List<GifData> list;
    private final VideoFilesHandler handler;

    public GifsFilesAdapter(List<GifData> list, VideoFilesHandler handler) {
        this.list = list;
        this.handler = handler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GifFileItemBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.gif_file_item, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageView image = holder.mBinding.gifPreview;

        Glide.with(image)
                .load(list.get(position).gifUri)
                .into(image);

        holder.mBinding.getRoot().setOnClickListener(view -> handler.onChoose(position, list.get(position).gifUri.toString(), list.get(position).videoName));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final GifFileItemBinding mBinding;

        public ViewHolder(@NonNull GifFileItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
    }

    public interface VideoFilesHandler {
        void onChoose(int position, String path, String name);
    }
}
