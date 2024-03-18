package com.example.greenrover;


import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SignupActivity extends AppCompatActivity{
    EditText UserName, UserPassword, FullName, Address, Postcode, Email, PhoneNum;
    Button LoginBtn, SignupBtn;
    TextView ErrorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FullName = findViewById(R.id.Fullname);
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
            String fullname = FullName.getText().toString();
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
