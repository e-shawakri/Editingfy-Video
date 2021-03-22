/*
 * Copyright (c) Emadyous Development
 */

package com.emadyous.editingfyVideos.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.emadyous.editingfyVideos.R;
import com.emadyous.editingfyVideos.databinding.VideoFileItemBinding;
import com.emadyous.editingfyVideos.model.VideoData;
import com.bumptech.glide.Glide;

import java.util.List;

public class SelectVideoAdapter extends RecyclerView.Adapter<SelectVideoAdapter.ViewHolder> {

    private final List<VideoData> list;
    private SelectionTracker<Long> mSelectionTracker;
    private OnSelectionChangedListener listener;
    private int selectedBackground;

    public void setmSelectionTracker(SelectionTracker<Long> mSelectionTracker, OnSelectionChangedListener listener) {
        this.mSelectionTracker = mSelectionTracker;
        this.listener = listener;
    }

    public void setSelectedBackground(int selectedBackground) {
        this.selectedBackground = selectedBackground;
    }

    public SelectVideoAdapter(List<VideoData> list) {
        this.list = list;
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
        holder.bind(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Details extends ItemDetailsLookup.ItemDetails<Long> {

        long position;

        public Details() {
            //nada
        }

        @Override
        public int getPosition() {
            return (int) position;
        }

        @Nullable
        @Override
        public Long getSelectionKey() {
            return position;
        }

        @Override
        public boolean inSelectionHotspot(@NonNull MotionEvent e) {
            return true;
        }
    }

    public static class DetailsLookup extends ItemDetailsLookup<Long> {

        private final RecyclerView recyclerView;

        public DetailsLookup(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Nullable
        @Override
        public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                if (viewHolder instanceof SelectVideoAdapter.ViewHolder) {
                    return ((SelectVideoAdapter.ViewHolder) viewHolder).getDetails();
                }
            }
            return null;
        }

    }

    public static class KeyProvider extends ItemKeyProvider<Long> {

        public KeyProvider(RecyclerView.Adapter adapter) {
            super(ItemKeyProvider.SCOPE_MAPPED);
        }

        @Nullable
        @Override
        public Long getKey(int position) {
            return (long) position;
        }

        @Override
        public int getPosition(@NonNull Long key) {
            long value = key;
            return (int) value;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private final VideoFileItemBinding mBinding;
        private final Details details;

        public ViewHolder(@NonNull VideoFileItemBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            details = new Details();
        }

        void bind(VideoData videoData, int position){
            details.position = position;
            mBinding.executePendingBindings();

            Glide.with(mBinding.videoPreview)
                    .load(videoData.videouri)
                    .into(mBinding.videoPreview);

            long key = details.getSelectionKey();


            if (mSelectionTracker != null) {
                Log.d("qqqq", "bind: " + key + "  " + details.getPosition());
                if (mSelectionTracker.isSelected(details.getSelectionKey())){
                    listener.onSelectionChanged(videoData, key, true);
                    mBinding.select.setBackgroundResource(selectedBackground);
                    mBinding.currentPosition.setVisibility(View.VISIBLE);
                    mBinding.getRoot().setActivated(true);
                } else {
                    listener.onSelectionChanged(videoData, key, false);
                    mBinding.select.setBackgroundColor(mBinding.getRoot().getResources().getColor(android.R.color.transparent));
                    mBinding.getRoot().setActivated(false);
                    mBinding.currentPosition.setVisibility(View.INVISIBLE);
                }
            }

        }

        Details getDetails() {
            return details;
        }
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged(VideoData videoData, long position, boolean isSelected);
    }
}
