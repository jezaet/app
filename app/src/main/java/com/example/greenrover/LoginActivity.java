package com.example.greenrover;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;




public class LoginActivity extends AppCompatActivity{

    EditText UserName, UserPassword;
    Button LoginBtn;
    TextView SignupBtn , ForgotPassword , ErrorMsg;
    CheckBox RememberMe;
    private FirebaseAuth mAuth;

    msgPopup d;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            finish();
            return;
        }




        //msg = new DialogBoxPopup();
        UserName = findViewById(R.id.loginUsername);
        UserPassword = findViewById(R.id.loginPassword);
        LoginBtn = findViewById(R.id.Login_button);
        SignupBtn = findViewById(R.id.Register_button);
        RememberMe = findViewById(R.id.RememberMe);
        ForgotPassword = findViewById(R.id.forgotPassword);


        SignupBtn.setOnClickListener(v -> ShowSignup());


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
        d = new msgPopup();
        d.showToast("Login Successful", this);
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("name","name");
//        startActivity(intent);
    }


}
