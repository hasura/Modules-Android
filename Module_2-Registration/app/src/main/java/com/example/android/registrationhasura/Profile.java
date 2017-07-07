package com.example.android.registrationhasura;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.FileDownloadResponseListener;

/**
 * Created by amogh on 1/6/17.
 */

public class Profile extends AppCompatActivity {

    EditText name;
    EditText status;
    ImageView picture;
    Button button;
    HasuraClient client;
    String fileId;

    int update = 0;//used as a flag

    private static final int CAMERA_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_information_activity);


        name = (EditText) findViewById(R.id.profile_name);
        status = (EditText) findViewById(R.id.profile_status);
        picture = (ImageView) findViewById(R.id.profile_picture);
        button = (Button) findViewById(R.id.profile_button);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        toolbar.setTitle("Set Up Your Profile");

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
                            Toast.makeText(Profile.this, "Welcome", Toast.LENGTH_SHORT).show();
                            name.setText(userDetailsList.get(0).getName());
                            status.setText(userDetailsList.get(0).getStatus());
                            picture.getLayoutParams().height = (int) Profile.this.getResources().getDimension(R.dimen.image_set_height);
                            picture.getLayoutParams().width = (int) Profile.this.getResources().getDimension(R.dimen.image_set_width);
                            client.useFileStoreService()
                                    .downloadFile("104_picture", new FileDownloadResponseListener() {
                                        @Override
                                        public void onDownloadComplete(byte[] bytes) {
                                            picture.setImageBitmap(Photo.getImage(bytes));
                                        }

                                        @Override
                                        public void onDownloadFailed(HasuraException e) {
                                            Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onDownloading(float v) {

                                        }
                                    });
                        }

                        @Override
                        public void onFailure(HasuraException e) {
                            Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Bitmap bitmap = ((BitmapDrawable)picture.getDrawable()).getBitmap();
                byte[] image = Photo.getBytes(bitmap);
                final UserDetails userDetails = new UserDetails();
                userDetails.setName(name.getText().toString().trim());
                userDetails.setStatus(status.getText().toString().trim());
                userDetails.setId(Hasura.getClient().getUser().getId());

                client.useFileStoreService()
                        .uploadFile(user.getId() + "_picture", image, "image/*", new FileUploadResponseListener() {
                                 @Override
                                 public void onUploadComplete(FileUploadResponse fileUploadResponse) {
                                     userDetails.setFileId(fileUploadResponse.getFile_id());
                                 }

                                 @Override
                                 public void onUploadFailed(HasuraException e) {
                                     Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_SHORT).show();
                                 }
                        });


                if(update == 0) {
                    client.useDataService()
                            .setRequestBody(new InsertQuery(userDetails))
                            .expectResponseType(ResponseMessage.class)
                            .enqueue(new Callback<ResponseMessage, HasuraException>() {
                                @Override
                                public void onSuccess(ResponseMessage responseMessage) {
                                    Toast.makeText(Profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(HasuraException e) {
                                    Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    client.useDataService()
                            .setRequestBody(new UpdateQuery(userDetails))
                            .expectResponseType(ResponseMessage.class)
                            .enqueue(new Callback<ResponseMessage, HasuraException>() {
                                @Override
                                public void onSuccess(ResponseMessage responseMessage) {
                                    Toast.makeText(Profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(HasuraException e) {
                                    Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }*/

                File dir = Environment.getExternalStorageDirectory();
                File[] files = dir.listFiles();

                Log.i("File",dir.toString());

                //Log.i("File", files.length + "");
                /*for (File file: files) {
                    Log.i("File", "Filename: " + file.getName());
                }*/


                //File yourFile = new File("/storage/emulated/0/DCIM/Camera/IMG_20170403_125715.jpg");
                File yourFile = new File("/document/3332-6230:DCIM/DCIM/Screenshots");

                File[] fileft = yourFile.listFiles();
                Log.i("File",yourFile.toString());

                Log.i("File", fileft.length + "");
                for (File file: fileft) {
                    Log.i("File", "Filename: " + file.getName());
                }


                /*client.useFileStoreService()
                        .uploadFile("SDcard_picture", yourFile, "image/*", new FileUploadResponseListener() {
                            @Override
                            public void onUploadComplete(FileUploadResponse fileUploadResponse) {
                                Toast.makeText(Profile.this, "Successful", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onUploadFailed(HasuraException e) {
                                Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_LONG).show();
                            }
                        });*/
            }
        });


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

