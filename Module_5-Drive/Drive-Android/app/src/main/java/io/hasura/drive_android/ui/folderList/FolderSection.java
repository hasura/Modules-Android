package io.hasura.drive_android.ui.folderList;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import io.hasura.drive_android.R;
import io.hasura.drive_android.models.HasuraFolder;
import io.hasura.drive_android.ui.home.HeaderViewHolder;

/**
 * Created by jaison on 31/03/17.
 */

public class FolderSection extends StatelessSection {

    List<HasuraFolder> folders;
    Listener listener;

    public FolderSection(Listener listener) {
        super(R.layout.layout_file_header, R.layout.layout_folder);
        this.folders = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public int getContentItemsTotal() {
        return folders.size();
    }

    public void setData(List<HasuraFolder> files) {
        this.folders = files;
    }

    public void addData(HasuraFolder folder){
        this.folders.add(folder);
        listener.onItemCountChanged(folders.size());

    }


    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        headerViewHolder.header.setText("Folders");
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FolderViewHolder holder = (FolderViewHolder) viewHolder;
        final HasuraFolder folder = folders.get(position);
        holder.fileTitle.setText(folder.getName());
        holder.parentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFileSelected(folder);
            }
        });
    }

    public interface Listener {
        void onFileSelected(HasuraFolder folder);
        void onItemCountChanged(int count);
    }

    static class FolderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_view)
        ImageView imageView;
        @BindView(R.id.file_title)
        TextView fileTitle;
        @BindView(R.id.parent_card)
        CardView parentCard;

        FolderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
