package com.example.android.inventoryapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by amogh on 28/6/17.
 */

public class CategoryListHolder extends RecyclerView.ViewHolder {

    RecyclerView recyclerView;
    TextView category;
    Button button;

    public CategoryListHolder(View itemView) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.items_recyclerview);
        category = (TextView) itemView.findViewById(R.id.category_name);
        button = (Button) itemView.findViewById(R.id.category_button);
    }
}
