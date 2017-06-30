package com.example.android.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.FileDownloadResponseListener;

/**
 * Created by amogh on 28/6/17.
 */

public class ShowProduct extends AppCompatActivity {

    TextView name;
    TextView price;
    TextView description;
    ImageView imageView;
    Button button;

    HasuraClient client = Hasura.getClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_description);

        name = (TextView) findViewById(R.id.product_description_name);
        price = (TextView) findViewById(R.id.product_description_price);
        description = (TextView) findViewById(R.id.product_description);
        imageView = (ImageView) findViewById(R.id.product_description_picture);
        button = (Button) findViewById(R.id.product_description_button);

        name.setText(Global.currentItem.getName());
        price.setText("Price :"+ Global.currentItem.getPrice() + "/-");
        description.setText(Global.currentItem.getDescription());

        client.useFileStoreService()
                .downloadFile(Global.currentItem.getFileId(), new FileDownloadResponseListener() {
                    @Override
                    public void onDownloadComplete(byte[] bytes) {
                        imageView.setImageBitmap(Photo.getImage(bytes));
                    }

                    @Override
                    public void onDownloadFailed(HasuraException e) {
                        Toast.makeText(ShowProduct.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDownloading(float v) {

                    }
                });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.currentItemList.add(Global.currentItem);

                Toast.makeText(ShowProduct.this, "Successfully added to Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_to_cart:
                Intent i = new Intent(ShowProduct.this, CartActivity.class);
                startActivity(i);
                return true;
        }
        return false;
    }
}
