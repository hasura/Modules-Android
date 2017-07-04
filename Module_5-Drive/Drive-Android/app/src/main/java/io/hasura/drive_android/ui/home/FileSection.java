package io.hasura.drive_android.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.hasura.drive_android.R;
import io.hasura.drive_android.models.HasuraFile;
import io.hasura.drive_android.utils.Image;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.FileDownloadResponseListener;

/**
 * Created by jaison on 31/03/17.
 */

public class FileSection extends Section {

    List<HasuraFile> files;
    Context context;
    Listener listener;
    HasuraClient client = Hasura.getClient();

    public FileSection(Context context, Listener listener) {
        super(R.layout.layout_file_header, R.layout.layout_file, R.layout.layout_loading, R.layout.layout_failed);
        this.files = new ArrayList<>();
        this.listener = listener;
        this.context = context;
    }

    @Override
    public int getContentItemsTotal() {
        return files.size();
    }

    public void setData(List<HasuraFile> files) {
        this.files = files;
        this.listener.onItemCountChanged(files.size());
    }

    public void addData(HasuraFile file) {
        this.files.add(file);
        this.listener.onItemCountChanged(files.size());
    }

    public void removeData(HasuraFile file) {
        this.files.remove(file);
        this.listener.onItemCountChanged(files.size());
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        headerViewHolder.header.setText("Passports");
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new FileViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final FileViewHolder holder = (FileViewHolder) viewHolder;
        final HasuraFile file = files.get(position);
        holder.fileTitle.setText(file.getName());
        holder.fileDescription.setText(file.getLast_modifiedString());

        client.useFileStoreService()
                .downloadFile(file.getId(), new FileDownloadResponseListener() {
                    @Override
                    public void onDownloadComplete(byte[] bytes) {
                        holder.imageView.setImageBitmap(Image.getImage(bytes));
                    }

                    @Override
                    public void onDownloadFailed(HasuraException e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDownloading(float v) {

                    }
                });

        holder.bottomLine.setVisibility(position < files.size() - 1 ? View.VISIBLE : View.INVISIBLE);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFileSelected(file);
            }
        });

        holder.optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFileOptionsSelected(file);
            }
        });
    }

    public interface Listener {
        void onItemCountChanged(int count);

        void onFileSelected(HasuraFile file);

        void onFileOptionsSelected(HasuraFile file);
    }
}
