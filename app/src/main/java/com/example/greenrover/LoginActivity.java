package com.example.greenrover;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.Loader;

import com.example.greenrover.data.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity{

    EditText UserName, UserPassword;
    Button LoginBtn;
    TextView SignupBtn , ForgotPassword ;
    CheckBox RememberMe;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    msgPopup d;
    LinearLayout PasswordLayout, UserNameLayout;
    Dialog loader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        if (mAuth.getCurrentUser() != null) {
            database.getReference("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                if(user.RememberMe){
                                    sucessfulLogin();
                                }else {
                                    mAuth.signOut();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            return;
        }





        UserName = findViewById(R.id.loginUsername);
        UserNameLayout = findViewById(R.id.loginUsernameLayout);
        UserPassword = findViewById(R.id.loginPassword);
        PasswordLayout = findViewById(R.id.loginPasswordLayout);
        LoginBtn = findViewById(R.id.Login_button);
        SignupBtn = findViewById(R.id.Register_button);
        RememberMe = findViewById(R.id.RememberMe);
        ForgotPassword = findViewById(R.id.forgotPassword);
        d = new msgPopup();


        SignupBtn.setOnClickListener(v -> ShowSignup());

        ForgotPassword.setOnClickListener(v -> ShowForgotPassword());

        LoginBtn.setOnClickListener(v -> CheckUser());


        UserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newFirstName = s.toString();
                if (!newFirstName.equals(UserName.toString())) {
                    UserNameLayout.setBackgroundResource(R.drawable.border);
                }
            }
        });

        UserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newFirstName = s.toString();
                if (!newFirstName.equals(UserPassword.toString())) {
                    PasswordLayout.setBackgroundResource(R.drawable.border);
                }
            }
        });

    }


    private void CheckUser(){
        showloadingScreen("Logging in...");
        String email = UserName.getText().toString();
        String password = UserPassword.getText().toString();

        if (password.isEmpty() && email.isEmpty()) {
            UserName.setError("Email is required");
            UserName.requestFocus();
            UserPassword.setError("Password is required");
            UserPassword.requestFocus();
            d.showToast("Please fill all fields", this);
            PasswordLayout.setBackgroundResource(R.drawable.error_border);
            UserNameLayout.setBackgroundResource(R.drawable.error_border);
            return;

        } else if(email.isEmpty()){
            UserName.setError("Email is required");
            UserName.requestFocus();
            d.showToast("Email is required", this);
            UserNameLayout.setBackgroundResource(R.drawable.error_border);
            return;

        } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            UserName.requestFocus();
            UserName.setError("please enter a valid email");
            UserNameLayout.setBackgroundResource(R.drawable.error_border);
            d.showToast("please enter a valid email E.g test@app.com", this);
            return;

        } else if(password.isEmpty()){
            UserPassword.setError("Password is required");
            UserPassword.requestFocus();
            d.showToast("Password is required", this);
            PasswordLayout.setBackgroundResource(R.drawable.error_border);
            return;
        }



        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        database.getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user = snapshot.getValue(User.class);
                                        if (user != null) {
                                            if(RememberMe.isChecked()){
                                                user.RememberMe = true;
                                                database.getReference("users")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .setValue(user);
                                            }
                                            sucessfulLogin();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    } else {
                        loader.dismiss();
                        Exception exception = task.getException();
                        if (exception != null) {
                            String errorMessage = exception.getMessage();
                            d.showToast("Login Failed. " + errorMessage, this);
                        } else {
                            d.showToast(" Login Failed. Please try again", this);
                        }

                    }
                });
    }


    private void ShowSignup(){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    private void ShowForgotPassword(){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void sucessfulLogin(){
        if (loader != null) {
            loader.dismiss();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void showloadingScreen(String text){
        loader = new Dialog(LoginActivity.this);

        Objects.requireNonNull(loader.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        loader.setContentView(R.layout.loadingsign);

        WindowManager.LayoutParams par = new WindowManager.LayoutParams();
        par.copyFrom(loader.getWindow().getAttributes());
        par.width = WindowManager.LayoutParams.MATCH_PARENT;
        par.height = WindowManager.LayoutParams.MATCH_PARENT;
        TextView loadingMessage = loader.findViewById(R.id.loadingMessage);
        loadingMessage.setText(text);
        loader.getWindow().setAttributes(par);
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);
        loader.show();

    }


}
