package com.example.android.signuphasura;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by amogh on 25/5/17.
 */

public class SignUpChoice extends AppCompatActivity {

    Button button_email;
    Button button_mobile;
    Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_choice_activity);

        button_email = (Button) findViewById(R.id.email);
        button_mobile = (Button) findViewById(R.id.mobile);
        button_register = (Button) findViewById(R.id.register);

        button_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpChoice.this,EmailSignup.class);
                startActivity(i);
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpChoice.this,Register.class);
                startActivity(i);
            }
        });

        button_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpChoice.this,MobileSignup.class);
                startActivity(i);
            }
        });

    }
}
