package com.example.greenrover;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText Email;
    TextView backToLogin;
    Button resetPassword;
    LinearLayout resetemail_layout;
    private FirebaseAuth mAuth;
    msgPopup d;
    Dialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            finish();
            return;
        }

        Email = findViewById(R.id.restemail);
        resetPassword = findViewById(R.id.reset_button);
        backToLogin = findViewById(R.id.resetbacktoLogin);
        resetemail_layout = findViewById(R.id.resetemail_layout);
        d = new msgPopup();


        resetPassword.setOnClickListener(v -> { CheckUser(); });

        backToLogin.setOnClickListener(v -> { showLogin(); });


        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newemail = s.toString();
                if (!newemail.equals(Email.toString())) {
                    resetemail_layout.setBackgroundResource(R.drawable.border);
                }
            }
        });

    }


    private void CheckUser() {
        showloadingScreen("Logging in...");
        String email = Email.getText().toString();

        if(email.isEmpty()) {
            Email.setError("Email is required");
            Email.requestFocus();
            d.showToast("Email is required", this);
            resetemail_layout.setBackgroundResource(R.drawable.error_border);
            loader.dismiss();
            return;
        }else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            Email.requestFocus();
            Email.setError("please enter a valid email");
            resetemail_layout.setBackgroundResource(R.drawable.error_border);
            d.showToast("please enter a valid email E.g test@app.com", this);
            loader.dismiss();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Email.setText("");
                d.showToast("Password reset link sent to your email If account exists", this);
                loader.dismiss();
            } else {
                loader.dismiss();
                Exception exception = task.getException();
                if (exception != null) {
                    String errorMessage = exception.getMessage();
                    d.showToast("request Failed. " + errorMessage, this);
                } else {
                    d.showToast(" request Failed. Please try again", this);
                }
            }
        });

    }


    private void showLogin() {
        if (loader != null) {
            loader.dismiss();
        }
        finish();
    }

    public void showloadingScreen(String text){
        loader = new Dialog(ForgotPasswordActivity.this);

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
