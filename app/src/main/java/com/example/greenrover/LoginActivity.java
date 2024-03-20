package com.example.greenrover;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class LoginActivity extends AppCompatActivity{

    EditText UserName, UserPassword;
    Button LoginBtn;
    TextView SignupBtn , ForgotPassword , ErrorMsg;
    CheckBox RememberMe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //msg = new DialogBoxPopup();
        UserName = findViewById(R.id.loginUsername);
        UserPassword = findViewById(R.id.loginPassword);
        LoginBtn = findViewById(R.id.Login_button);
        SignupBtn = findViewById(R.id.Register_button);
        ErrorMsg = findViewById(R.id.loginError);
        RememberMe = findViewById(R.id.RememberMe);
        ForgotPassword = findViewById(R.id.forgotPassword);


        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSignup();
            }
        });


        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = UserName.getText().toString();
                String password = UserPassword.getText().toString();
                if (username.equals("p") & password.equals("p")){
                    sucessfulLogin();
                }
            }
        });

    }





    private void ShowSignup(){
        Intent intent = new Intent(this, SignupActivity.class);
        intent.putExtra("name","reg");
        startActivity(intent);
    }

    private void sucessfulLogin(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name","name");
        startActivity(intent);
    }


}
