package com.example.android.inventoryapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.FileDownloadResponseListener;

/**
 * Created by amogh on 28/6/17.
 */


public class ItemListAdapter extends RecyclerView.Adapter<ItemListHolder> {

    List<ItemModel> itemList = new ArrayList<>();

    Context context;


    public interface Interactor{
        void onItemClicked(int position,ItemModel item);
    }

    public ItemListAdapter(Interactor interactor){
        this.interactor = interactor;
    }

    Interactor interactor;


    @Override
    public ItemListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        return new ItemListHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemListHolder holder, final int position) {
        final ItemModel item = itemList.get(position);
        holder.name.setText(item.getName());
        holder.price.setText("Price : " + item.getPrice() + "/-");

        HasuraClient client = Hasura.getClient();

        client.useFileStoreService()
                .downloadFile(item.getFileId(), new FileDownloadResponseListener() {
                    @Override
                    public void onDownloadComplete(byte[] bytes) {
                        holder.picture.setImageBitmap(Photo.getImage(bytes));
                    }

                    @Override
                    public void onDownloadFailed(HasuraException e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDownloading(float v) {

                    }
                });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interactor.onItemClicked(position,item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItemList(List<ItemModel> itemList){

        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public void addItem(ItemModel item){
        itemList.add(item);
        notifyDataSetChanged();
    }
}
