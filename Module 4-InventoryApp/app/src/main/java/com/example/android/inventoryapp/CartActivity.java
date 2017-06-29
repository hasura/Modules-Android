package com.example.android.inventoryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by amogh on 29/6/17.
 */

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button button;

    LinearLayoutManager linearLayoutManager;
    CartListAdapter adapter;

    double total = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        button = (Button) findViewById(R.id.cart_button);
        recyclerView = (RecyclerView) findViewById(R.id.cart_recyclerview);

        linearLayoutManager = new LinearLayoutManager(CartActivity.this);
        adapter = new CartListAdapter(new CartListAdapter.Interactor() {
            @Override
            public void totalInitialization(double subTotal) {
                total = total + subTotal;
            }

            @Override
            public void onIncreaseButtonClicked(double subTotal) {
                total = total + subTotal;
            }

            @Override
            public void onDecreaseButtonClicked(double subTotal) {
                total = total - subTotal;
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setItemList(Global.currentItemList);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CartActivity.this, "Total is :" + total, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
