package com.example.greenrover;


import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SignupActivity extends AppCompatActivity{
    EditText UserName, UserPassword, Address, Postcode, Email, PhoneNum , FirstName, LastName;
    Button SignupBtn;
    TextView ErrorMsg , LoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FirstName = findViewById(R.id.Firstname);
        LastName = findViewById(R.id.Lastname);
        Address = findViewById(R.id.SignupAddress);
        Postcode = findViewById(R.id.SignupPostcode);
        Email = findViewById(R.id.signupEmail);
        PhoneNum = findViewById(R.id.signupPhone);
        UserName = findViewById(R.id.signupUsername);
        UserPassword = findViewById(R.id.signupPassword);
        LoginBtn = findViewById(R.id.signupBack_button);
        SignupBtn = findViewById(R.id.SignupRegister_button);
        ErrorMsg = findViewById(R.id.loginError);

        LoginBtn.setOnClickListener(v -> {
            ShowLogin();
        });

        SignupBtn.setOnClickListener(v -> {
            String username = UserName.getText().toString();
            String password = UserPassword.getText().toString();
            String firstname = FirstName.getText().toString();
            String lastname = LastName.getText().toString();
            String address = Address.getText().toString();
            String postcode = Postcode.getText().toString();
            String email = Email.getText().toString();
            String phone = PhoneNum.getText().toString();
        });

    }


    private void ShowLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
