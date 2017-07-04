package io.hasura.drive_android.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import io.hasura.drive_android.R;
import io.hasura.drive_android.models.UploadingFile;

/**
 * Created by jaison on 31/03/17.
 */

public class UploadingFileSection extends StatelessSection {

    List<UploadingFile> files;
    InteractionListener listener;
    Context context;

    public UploadingFileSection(Context context, InteractionListener listener) {
        super(R.layout.layout_file_header, R.layout.layout_file_uploading);
        this.files = new ArrayList<>();
        this.listener = listener;
        this.context = context;
    }

    public void setData(List<UploadingFile> files) {
        this.files = files;
        this.listener.onItemCountChanged(files.size());
    }

    public void addData(UploadingFile file) {
        this.files.add(file);
        this.listener.onItemCountChanged(files.size());
    }

    public void removeData(UploadingFile file) {
        this.files.remove(file);
        this.listener.onItemCountChanged(files.size());
    }

    @Override
    public int getContentItemsTotal() {
        return files.size();
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        headerViewHolder.header.setText("Uploading");
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new UploadingFileViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final UploadingFile file = files.get(position);
        UploadingFileViewHolder holder = (UploadingFileViewHolder) viewHolder;

        Glide.with(context)
                .load(file.getImageUri())
                .centerCrop()
                .crossFade()
                .into(holder.imageView);

        holder.fileTitle.setText(file.getName());
        holder.buttonStopUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStopUploadClicked(file);
            }
        });

        holder.bottomLine.setVisibility(position < files.size() - 1 ? View.VISIBLE : View.INVISIBLE);
    }

    public interface InteractionListener {
        void onStopUploadClicked(UploadingFile file);
        void onItemCountChanged(int count);
    }
}
