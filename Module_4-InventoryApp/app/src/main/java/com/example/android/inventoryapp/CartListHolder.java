package com.example.android.inventoryapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by amogh on 29/6/17.
 */

public class CartListHolder extends RecyclerView.ViewHolder {

    TextView name;
    ImageView imageView;
    TextView quantity;
    TextView price;
    Button increase;
    Button decrease;

    public CartListHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.cart_product_name);
        quantity = (TextView) itemView.findViewById(R.id.cart_quantity);
        price = (TextView) itemView.findViewById(R.id.cart_price);
        imageView = (ImageView) itemView.findViewById(R.id.cart_cross_button);
        increase = (Button) itemView.findViewById(R.id.quantity_increase);
        decrease = (Button) itemView.findViewById(R.id.quantity_decrease);
    }
}
