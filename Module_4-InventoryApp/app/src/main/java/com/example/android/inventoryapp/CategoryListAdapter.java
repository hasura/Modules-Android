package com.example.android.inventoryapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amogh on 28/6/17.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListHolder> {

    List<CategoryModel> categoryItems = new ArrayList<>();

    List<ItemModel> itemlist = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    private Context context;

    public interface Interactor{
        void onButtonClicked(List<ItemModel> itemList);
    }

    public CategoryListAdapter(Interactor interactor){
        this.interactor = interactor;
    }

    Interactor interactor;

    @Override
    public CategoryListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new CategoryListHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryListHolder holder, int position) {
        final CategoryModel categoryItem = categoryItems.get(position);

        holder.category.setText(categoryItem.getCategory());
        gridLayoutManager = new GridLayoutManager(context,2);
        ItemListAdapter adapter = new ItemListAdapter(new ItemListAdapter.Interactor() {
            @Override
            public void onItemClicked(int position, ItemModel item) {
                Global.currentItem = item;
                Intent i = new Intent(context,ShowProduct.class);
                context.startActivity(i);
            }
        });

        holder.recyclerView.setLayoutManager(gridLayoutManager);
        holder.recyclerView.setAdapter(adapter);

        if(categoryItem.itemList.size()>4){
            itemlist = categoryItem.itemList.subList(0,3);
        }else {
            itemlist = categoryItem.itemList;
        }
        adapter.setItemList(itemlist);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, categoryItem.itemList.size()+"", Toast.LENGTH_LONG).show();
                interactor.onButtonClicked(categoryItem.itemList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryItems.size();
    }

    public void addCategoryItem(CategoryModel categoryItem){
        categoryItems.add(categoryItem);
        notifyDataSetChanged();
    }

    public void setCategoryItems(List<CategoryModel> categoryItems){
        this.categoryItems = categoryItems;
        notifyDataSetChanged();
    }
}
