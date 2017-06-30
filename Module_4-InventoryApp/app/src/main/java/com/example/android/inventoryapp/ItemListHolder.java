package com.example.android.inventoryapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by amogh on 28/6/17.
 */

public class ItemListHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView price;
    ImageView picture;
    Button button;

    public ItemListHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.item_name);
        price = (TextView) itemView.findViewById(R.id.item_price);
        picture = (ImageView) itemView.findViewById(R.id.picture);
        button = (Button) itemView.findViewById(R.id.button);
    }
}
