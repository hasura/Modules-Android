package com.example.android.registrationhasura;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import io.hasura.sdk.auth.HasuraUser;
import io.hasura.sdk.core.Callback;
import io.hasura.sdk.core.Hasura;
import io.hasura.sdk.core.HasuraException;

/**
 * Created by amogh on 1/6/17.
 */

public class Profile extends AppCompatActivity {

    EditText name;
    EditText status;
    ImageView picture;
    Button button;

    int update = 0;//used as a flag

    //private static final String DATABASE_NAME = "chatapp";
    //private static final int DATABASE_VERSION = 2;

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

        final HasuraUser user = Hasura.currentUser();



        if(Hasura.currentUser() != null){

            //Make a call and get details stored in database and update the corresponding views.

            user.getQueryBuilder()
                    .useDataService()
                    .setRequestBody(new SelectQuery(user.getId()))
                    .expectResponseTypeArrayOf(UserDetails.class)
                    .build()
                    .executeAsync(new Callback<List<UserDetails>, HasuraException>() {
                        @Override
                        public void onSuccess(List<UserDetails> userDetailsList) {

                            if(userDetailsList.size() > 0)
                                update = 1;

                            Toast.makeText(Profile.this, "Welcome", Toast.LENGTH_SHORT).show();
                            name.setText(userDetailsList.get(0).getName());
                            status.setText(userDetailsList.get(0).getStatus());
                            picture.getLayoutParams().height = (int) Profile.this.getResources().getDimension(R.dimen.image_set_height);
                            picture.getLayoutParams().width = (int) Profile.this.getResources().getDimension(R.dimen.image_set_width);
                        }

                        @Override
                        public void onFailure(HasuraException e) {
                            Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });

            /*picture.setImageBitmap(Photo.getImage(details.getPicture()));*/
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
                Bitmap bitmap = ((BitmapDrawable)picture.getDrawable()).getBitmap();
                //byte[] image = Photo.getBytes(bitmap);
                UserDetails userDetails = new UserDetails();
                userDetails.setName(name.getText().toString().trim());
                userDetails.setStatus(status.getText().toString().trim());
                //userDetails.setPicture(image);
                userDetails.setId(Hasura.currentUser().getId());


                if(update == 0) {
                    user.getQueryBuilder()
                            .useDataService()
                            .setRequestBody(new InsertQuery(userDetails))
                            .expectResponseOfType(ResponseMessage.class)
                            .build()
                            .executeAsync(new Callback<ResponseMessage, HasuraException>() {
                                @Override
                                public void onSuccess(ResponseMessage response) {
                                    Toast.makeText(Profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(HasuraException e) {
                                    Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    user.getQueryBuilder()
                            .useDataService()
                            .setRequestBody(new UpdateQuery(userDetails))
                            .expectResponseOfType(ResponseMessage.class)
                            .build()
                            .executeAsync(new Callback<ResponseMessage, HasuraException>() {
                                @Override
                                public void onSuccess(ResponseMessage response) {
                                    Toast.makeText(Profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(HasuraException e) {
                                    Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
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

