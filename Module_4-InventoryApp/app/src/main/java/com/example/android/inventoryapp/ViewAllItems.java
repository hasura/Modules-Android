package com.example.android.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;

/**
 * Created by amogh on 28/6/17.
 */

public class ViewAllItems extends Fragment{

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ItemListAdapter adapter;

    HasuraUser user = Hasura.getClient().getUser();
    HasuraClient client = Hasura.getClient();

    View parentViewHolder;

    String category;

    boolean loaditems = true;

    private static final String ARG_VALUE = "argValue";

    public static ViewAllItems NewInstance(String value) {
        Bundle args = new Bundle();
        args.putString(ARG_VALUE, value);

        ViewAllItems fragment = new ViewAllItems();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        this.category = args.getString(ARG_VALUE, null);
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        parentViewHolder = inflater.inflate(R.layout.all_products,container,false);

        recyclerView = (RecyclerView) parentViewHolder.findViewById(R.id.view_all_recyclerview);

        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        adapter = new ItemListAdapter(new ItemListAdapter.Interactor() {
            @Override
            public void onItemClicked(int position, ItemModel item) {
                Global.currentItem = item;
                Intent i = new Intent(getActivity().getApplicationContext(),ShowProduct.class);
                startActivity(i);
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        client.useDataService()
                .setRequestBody(new SelectQuery(category))
                .expectResponseTypeArrayOf(ItemModel.class)
                .enqueue(new Callback<List<ItemModel>, HasuraException>() {
                             @Override
                             public void onSuccess(List<ItemModel> itemModels) {
                                 loaditems = false;
                                 adapter.setItemList(itemModels);
                                 }

                             @Override
                             public void onFailure(HasuraException e) {
                                 Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                             }
                         });

        return parentViewHolder;
    }
}
