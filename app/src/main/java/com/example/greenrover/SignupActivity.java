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

import com.example.greenrover.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class SignupActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    EditText UserPassword, Address, Postcode, Email, PhoneNum , FirstName, LastName;
    Button SignupBtn;
    TextView  LoginBtn;
    msgPopup d;
    LinearLayout FirstNamebox , LastNamebox , Addressbox , Postcodebox , Emailbox , PhoneNumbox , UserPasswordbox;
    Dialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            finish();
            return;
        }


        FirstName = findViewById(R.id.Firstname);
        FirstNamebox = findViewById(R.id.Firstnamebox);
        LastName = findViewById(R.id.Lastname);
        LastNamebox = findViewById(R.id.Lastnamebox);
        Address = findViewById(R.id.SignupAddress);
        Addressbox = findViewById(R.id.SignupAddressbox);
        Postcode = findViewById(R.id.SignupPostcode);
        Postcodebox = findViewById(R.id.SignupPostcodebox);
        Email = findViewById(R.id.signupEmail);
        Emailbox = findViewById(R.id.signupEmailbox);
        PhoneNum = findViewById(R.id.signupPhone);
        PhoneNumbox = findViewById(R.id.signupPhonebox);
        UserPassword = findViewById(R.id.signupPassword);
        UserPasswordbox = findViewById(R.id.signupPasswordbox);
        LoginBtn = findViewById(R.id.signupBack_button);
        SignupBtn = findViewById(R.id.SignupRegister_button);
        d = new msgPopup();


        LoginBtn.setOnClickListener(v -> {
            ShowLogin();
        });


        SignupBtn.setOnClickListener(v -> {
            boolean valid = Checkdata();
            if (valid){RegisterNewUser();}
        });

        FirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newFirstName = s.toString();
                if (!newFirstName.equals(FirstName.toString())) {
                    FirstNamebox.setBackgroundResource(R.drawable.border);
                }
            }
        });

        LastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newLastName = s.toString();
                if (!newLastName.equals(LastName.toString())) {
                    LastNamebox.setBackgroundResource(R.drawable.border);
                }
            }
        });

        Address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newAddress = s.toString();
                if (!newAddress.equals(Address.toString())) {
                    Addressbox.setBackgroundResource(R.drawable.border);
                }
            }
        });

        Postcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newPostcode = s.toString();
                if (!newPostcode.equals(Postcode.toString())) {
                    Postcodebox.setBackgroundResource(R.drawable.border);
                }
            }
        });

        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newEmail = s.toString();
                if (!newEmail.equals(Email.toString())) {
                    Emailbox.setBackgroundResource(R.drawable.border);
                }
            }
        });

        PhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newPhoneNum = s.toString();
                if (!newPhoneNum.equals(PhoneNum.toString())) {
                    PhoneNumbox.setBackgroundResource(R.drawable.border);
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
                String newPassword = s.toString();
                if (!newPassword.equals(UserPassword.toString())) {
                    UserPasswordbox.setBackgroundResource(R.drawable.border);
                }
            }
        });





    }


    private void ShowLogin(){
        if (loader != null) {
            loader.dismiss();
        }
        finish();
    }


    private Boolean Checkdata(){
        boolean valid = true;
        String password = UserPassword.getText().toString();
        String email = Email.getText().toString();
        String phone = PhoneNum.getText().toString();
        String address = Address.getText().toString();
        String postcode = Postcode.getText().toString();
        String firstname = FirstName.getText().toString();
        String lastname = LastName.getText().toString();

        if (password.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || postcode.isEmpty() || firstname.isEmpty() || lastname.isEmpty()) {
            d.showToast("Please fill all fields", this);
            valid = false;
        }

        if (firstname.isEmpty()) {
            FirstName.requestFocus();
            FirstName.setError("First name is required");
            FirstNamebox.setBackgroundResource(R.drawable.error_border);
        }

        if (lastname.isEmpty()) {
            LastName.requestFocus();
            LastName.setError("Last name is required");
            LastNamebox.setBackgroundResource(R.drawable.error_border);
        }

        if (address.isEmpty()) {
            Address.requestFocus();
            Address.setError("Address is required");
            Addressbox.setBackgroundResource(R.drawable.error_border);
        }

        if (postcode.isEmpty()) {
            Postcode.requestFocus();
            Postcode.setError("Postcode is required");
            Postcodebox.setBackgroundResource(R.drawable.error_border);
        } else if (!postcode.matches("^[A-Z]{1,2}[0-9]{1,2}[A-Z]? [0-9][A-Z]{2}$")) {
            valid = false;
            Postcode.requestFocus();
            Postcode.setError("please enter a valid postcode e.g. AB12 3CD");
            Postcodebox.setBackgroundResource(R.drawable.error_border);
        }

        if (phone.isEmpty()) {
            PhoneNum.requestFocus();
            PhoneNum.setError("Phone number is required");
            PhoneNumbox.setBackgroundResource(R.drawable.error_border);
        } else if (!phone.matches("^(\\+44|0)7\\d{9}$")) {
            valid = false;
            PhoneNum.requestFocus();
            PhoneNum.setError("please enter a valid phone number");
            PhoneNumbox.setBackgroundResource(R.drawable.error_border);
        }

        if (email.isEmpty()) {
            Email.requestFocus();
            Email.setError("Email is required");
            Emailbox.setBackgroundResource(R.drawable.error_border);
        } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            valid = false;
            Email.requestFocus();
            Email.setError("please enter a valid email");
            Emailbox.setBackgroundResource(R.drawable.error_border);
        }

        if (password.isEmpty()) {
            UserPassword.requestFocus();
            UserPassword.setError("Password is required");
            UserPasswordbox.setBackgroundResource(R.drawable.error_border);
        }

        return valid;
    }

    private void RegisterNewUser(){
        showloadingScreen("Registering User...");
        String password = UserPassword.getText().toString();
        String email = Email.getText().toString();
        String phone = PhoneNum.getText().toString();
        String address = Address.getText().toString();
        String postcode = Postcode.getText().toString();
        String firstname = FirstName.getText().toString();
        String lastname = LastName.getText().toString();



        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User(firstname, lastname, address, postcode, phone, email, false);
                        FirebaseDatabase.getInstance().getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user);
                        mAuth.signOut();
                        ShowLogin();
                    } else {
                        loader.dismiss();
                        Exception exception = task.getException();
                        if (exception != null) {
                            String errorMessage = exception.getMessage();
                            // Display error message (e.g., showToast)
                            d.showToast("Authentication failed. " + errorMessage, this);
                        } else {
                            // Handle case when exception is null
                            d.showToast("Authentication failed. Try again", this);
                        }


                    }
                });
    }


    public void showloadingScreen(String text){
        loader = new Dialog(SignupActivity.this);

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
