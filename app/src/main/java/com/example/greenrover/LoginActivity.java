package com.example.greenrover;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;



public class LoginActivity extends AppCompatActivity{

    EditText UserName, UserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserName = findViewById(R.id.loginUsername);
        UserPassword = findViewById(R.id.loginPassword);

    }
}
