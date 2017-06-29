package com.example.android.inventoryapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amogh on 29/6/17.
 */

public class CartListAdapter extends RecyclerView.Adapter<CartListHolder> {

    List<ItemModel> itemList = new ArrayList<>();

    double total = 0;
    int quantity;

    Context context;

    public interface Interactor{
        void totalInitialization(double subTotal);
        void onIncreaseButtonClicked(double subTotal);
        void onDecreaseButtonClicked(double subTotal);
    }

    public CartListAdapter(Interactor interactor){
        this.interactor = interactor;
    }

    Interactor interactor;


    @Override
    public CartListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new CartListHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartListHolder holder, int position) {
        final ItemModel item = itemList.get(position);
        holder.name.setText(item.getName());
        holder.price.setText(item.getPrice() + "/-");

        quantity = getNumber(holder.quantity.getText().toString());

        String[] price_item = holder.price.getText().toString().split("/");

        int price = getNumber(price_item[0]);

        total = price*quantity;

        interactor.totalInitialization(total);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity == 1)
                    removeItem(item);

                quantity--;
                holder.quantity.setText(quantity+"");
                interactor.onDecreaseButtonClicked(total);
            }
        });

        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity>0) {
                    quantity--;
                    holder.quantity.setText(quantity + "");
                    interactor.onDecreaseButtonClicked(total);
                }else if(quantity == 0){
                    removeItem(item);
                }
            }
        });

        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                holder.quantity.setText(quantity+"");
                interactor.onIncreaseButtonClicked(total);
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

    public void removeItem(ItemModel item){
        itemList.remove(item);
        notifyDataSetChanged();
    }

    public int getNumber(String string){
        char[] input = string.toCharArray();
        int i;
        int id = 0;

        for (i = 0;i < input.length;i++){
            id = id * 10;
            id = id + (input[i] - '0');
        }

        return id;
    }
}
