package com.example.android.chatmodule;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by amogh on 1/6/17.
 */

public class Profile extends AppCompatActivity {

    EditText name;
    EditText status;
    ImageView picture;
    Button button;

    int update = 0;

    HasuraClient client = Hasura.getClient();
    HasuraUser user = Hasura.getClient().getUser();

    private static final String DATABASE_NAME = "chatapp";
    private static final int DATABASE_VERSION = 2;

    private static final int CAMERA_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_information_activity);

        UserDetails details;
        final DataBaseHandler db = new DataBaseHandler(Profile.this,DATABASE_NAME,null,DATABASE_VERSION);

        name = (EditText) findViewById(R.id.profile_name);
        status = (EditText) findViewById(R.id.profile_status);
        picture = (ImageView) findViewById(R.id.profile_picture);
        button = (Button) findViewById(R.id.profile_button);

        if(Hasura.getClient().getUser() != null){

            //Make a call and get details stored in database and update the corresponding views.

            client.useDataService()
                    .setRequestBody(new SelectQuery(user.getId()))
                    .expectResponseTypeArrayOf(UserDetails.class)
                    .enqueue(new Callback<List<UserDetails>, HasuraException>() {
                        @Override
                        public void onSuccess(List<UserDetails> userDetailsList) {
                            if(userDetailsList.size() > 0)
                                update = 1;

                            Toast.makeText(Profile.this, "Welcome", Toast.LENGTH_SHORT).show();
                            name.setText(userDetailsList.get(0).getName());
                            status.setText(userDetailsList.get(0).getStatus());
                            //picture.getLayoutParams().height = (int) Profile.this.getResources().getDimension(R.dimen.image_set_height);
                            //picture.getLayoutParams().width = (int) Profile.this.getResources().getDimension(R.dimen.image_set_width);
                        }

                        @Override
                        public void onFailure(HasuraException e) {
                            Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });



            /*picture.setImageBitmap(Photo.getImage(details.getPicture()));*/
        }else{
            details = db.getProfile();
            if(details.getId() == user.getId()){
                name.setText(details.getName());
                status.setText(details.getStatus());
                //picture.setImageBitmap(Picture.getImage(details.getPicture()));
            }
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
                byte[] image = Picture.getBytes(bitmap);
                UserDetails userDetails = new UserDetails();
                userDetails.setName(name.getText().toString().trim());
                userDetails.setStatus(status.getText().toString().trim());
                //userDetails.setPicture(image);
                userDetails.setId(user.getId());

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
                }


                db.insertUserDetails(userDetails);
                db.close();

                Intent i = new Intent(Profile.this,ContactsActivity.class);
                startActivity(i);
                finish();

            }
        });


}
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        if(requestcode == CAMERA_REQUEST && resultcode == Activity.RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            picture.getLayoutParams().height = (int) Profile.this.getResources().getDimension(R.dimen.image_set_height);
            picture.getLayoutParams().width = (int) Profile.this.getResources().getDimension(R.dimen.image_set_width);
            picture.setImageBitmap(photo);
        }
    }


}
