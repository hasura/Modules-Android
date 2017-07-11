package com.example.android.chatmodule;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.model.response.FileUploadResponse;
import io.hasura.sdk.responseListener.FileDownloadResponseListener;
import io.hasura.sdk.responseListener.FileUploadResponseListener;

/**
 * Created by amogh on 1/6/17.
 */

public class ProfileActivity extends AppCompatActivity {

    EditText name;
    EditText status;
    ImageView picture;
    Button button;
    HasuraClient client;
    String fileId;
    Context context;

    int update = 0;//used as a flag

    private static final int CAMERA_REQUEST = 100;

    private static final int PERMISSIONS_REQUEST_CAMERA = 200;

    @Override
    public void onBackPressed(){
        Intent i = new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_information);

        context = getApplicationContext();

        name = (EditText) findViewById(R.id.profile_name);
        status = (EditText) findViewById(R.id.profile_status);
        picture = (ImageView) findViewById(R.id.profile_picture);
        button = (Button) findViewById(R.id.profile_button);

        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayShowTitleEnabled(true);
        toolbar.setTitle("Your ProfileActivity");

        toolbar.setHomeButtonEnabled(true);
        toolbar.setDisplayHomeAsUpEnabled(true);

        final HasuraUser user = Hasura.getClient().getUser();

        client = Hasura.getClient();

        if(Hasura.getClient().getUser() != null){

            client.useDataService()
                    .setRequestBody(new SelectQuery(user.getId()))
                    .expectResponseTypeArrayOf(UserDetails.class)
                    .enqueue(new Callback<List<UserDetails>, HasuraException>() {
                        @Override
                        public void onSuccess(List<UserDetails> userDetailsList) {
                            if(userDetailsList.size() > 0)
                                update = 1;

                            fileId = userDetailsList.get(0).getFileId();
                            Toast.makeText(ProfileActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                            name.setText(userDetailsList.get(0).getName());
                            status.setText(userDetailsList.get(0).getStatus());
                            picture.getLayoutParams().height = (int) ProfileActivity.this.getResources().getDimension(R.dimen.image_set_height);
                            picture.getLayoutParams().width = (int) ProfileActivity.this.getResources().getDimension(R.dimen.image_set_width);
                            client.useFileStoreService()
                                    .downloadFile(userDetailsList.get(0).getFileId(), new FileDownloadResponseListener() {
                                        @Override
                                        public void onDownloadComplete(byte[] bytes) {
                                            picture.setImageBitmap(Picture.getImage(bytes));
                                        }

                                        @Override
                                        public void onDownloadFailed(HasuraException e) {
                                            Toast.makeText(ProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onDownloading(float v) {

                                        }
                                    });
                        }

                        @Override
                        public void onFailure(HasuraException e) {
                            Toast.makeText(ProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable)picture.getDrawable()).getBitmap();
                byte[] image = Picture.getBytes(bitmap);
                final UserDetails userDetails = new UserDetails();
                userDetails.setName(name.getText().toString().trim());
                userDetails.setStatus(status.getText().toString().trim());
                userDetails.setId(Hasura.getClient().getUser().getId());

                client.useFileStoreService()
                        .uploadFile(image, "image/*", new FileUploadResponseListener() {
                                 @Override
                                 public void onUploadComplete(FileUploadResponse fileUploadResponse) {
                                     userDetails.setFileId(fileUploadResponse.getFile_id());
                                     if (update == 0) {
                                         userDetails.setMobile(Global.mobile);
                                         client.useDataService()
                                                 .setRequestBody(new InsertQuery(userDetails))
                                                 .expectResponseType(ResponseMessage.class)
                                                 .enqueue(new Callback<ResponseMessage, HasuraException>() {
                                                     @Override
                                                     public void onSuccess(ResponseMessage responseMessage) {
                                                         Toast.makeText(ProfileActivity.this, "ProfileActivity updated successfully", Toast.LENGTH_SHORT).show();
                                                     }

                                                     @Override
                                                     public void onFailure(HasuraException e) {
                                                         Toast.makeText(ProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                     }
                                                 });
                                     } else {
                                         client.useDataService()
                                                 .setRequestBody(new UpdateQuery(userDetails))
                                                 .expectResponseType(ResponseMessage.class)
                                                 .enqueue(new Callback<ResponseMessage, HasuraException>() {
                                                     @Override
                                                     public void onSuccess(ResponseMessage responseMessage) {
                                                         Toast.makeText(ProfileActivity.this, "ProfileActivity updated successfully", Toast.LENGTH_SHORT).show();
                                                     }

                                                     @Override
                                                     public void onFailure(HasuraException e) {
                                                         Toast.makeText(ProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                     }
                                                 });
                                     }
                                 }

                                 @Override
                                 public void onUploadFailed(HasuraException e) {
                                     Toast.makeText(ProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                 }
                        });

            }
        });


    }

    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAMERA_REQUEST);
        }
    }

    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        if(requestcode == CAMERA_REQUEST && resultcode == Activity.RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            picture.getLayoutParams().height = (int) this.getResources().getDimension(R.dimen.image_set_height);
            picture.getLayoutParams().width = (int) this.getResources().getDimension(R.dimen.image_set_width);
            picture.setImageBitmap(photo);
        }
    }
}

