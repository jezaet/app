package com.example.greenrover;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.greenrover.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AccountFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    EditText Address, Postcode, Email, PhoneNum , FirstName, LastName, NewPassword;
    LinearLayout FirstNamebox , LastNamebox , Addressbox , Postcodebox , Emailbox , PhoneNumbox;
    msgPopup d;
    Button save;
    User user;
    Dialog logoutDialog;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        FirstNamebox = rootView.findViewById(R.id.FirstNamebox);
        FirstName = rootView.findViewById(R.id.Firstname);
        LastNamebox = rootView.findViewById(R.id.LastNamebox);
        LastName = rootView.findViewById(R.id.Lastname);
        Addressbox = rootView.findViewById(R.id.Addressbox);
        Address = rootView.findViewById(R.id.Address);
        Postcodebox = rootView.findViewById(R.id.Postcodebox);
        Postcode = rootView.findViewById(R.id.Postcode);
        Emailbox = rootView.findViewById(R.id.Emailbox);
        Email = rootView.findViewById(R.id.Email);
        PhoneNumbox = rootView.findViewById(R.id.Phonebox);
        PhoneNum = rootView.findViewById(R.id.Phone);
        NewPassword = rootView.findViewById(R.id.newPassword);
        save = rootView.findViewById(R.id.update_button);
        d = new msgPopup();


        GetData();

        save.setOnClickListener(v -> {
            boolean valid = Checkdata();
            if (valid){UpdateUser();}
            if (!NewPassword.getText().toString().isEmpty()){
                updatePassword();
            }
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

        ;









        return rootView;
    }

    private void GetData(){
        database.getReference("users")
                .child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        if (user != null) {
                            FirstName.setText(user.FirstName);
                            LastName.setText(user.LastName);
                            Address.setText(user.Address);
                            Postcode.setText(user.Postcode);
                            Email.setText(user.Email);
                            PhoneNum.setText(user.PhoneNum);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        d.showToast("Error: " + error.getMessage(), getActivity());
                    }
                });
    }

    private Boolean Checkdata(){
        boolean valid = true;
        String email = Email.getText().toString();
        String phone = PhoneNum.getText().toString();
        String address = Address.getText().toString();
        String postcode = Postcode.getText().toString();
        String firstname = FirstName.getText().toString();
        String lastname = LastName.getText().toString();


        if ( email.isEmpty() || phone.isEmpty() || address.isEmpty() || postcode.isEmpty() || firstname.isEmpty() || lastname.isEmpty()) {
            d.showToast("Please fill all fields except password", getActivity());
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




        return valid;
    }

    private void UpdateUser(){
        user.FirstName = FirstName.getText().toString();
        user.LastName = LastName.getText().toString();
        user.Address = Address.getText().toString();
        user.Postcode = Postcode.getText().toString();
        user.Email = Email.getText().toString();
        user.PhoneNum = PhoneNum.getText().toString();

        database.getReference("users")
                .child(mAuth.getCurrentUser().getUid())
                .setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(NewPassword.getText().toString().isEmpty()){
                            d.showToast("User updated", getActivity());
                        }

                    } else {
                        d.showToast("Error: " + task.getException().getMessage(), getActivity());
                    }
                });
    }

    private void updatePassword(){
        mAuth.getCurrentUser().updatePassword(NewPassword.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        d.showToast("Password updated", getActivity());
                    } else {

                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthException) {
                            FirebaseAuthException e = (FirebaseAuthException) exception;
                            String errorCode = e.getErrorCode();
                            if (errorCode.equals("ERROR_REQUIRES_RECENT_LOGIN")) {
                                showlogoutScreen("Error: " + task.getException().getMessage());


                            } else {
                                d.showToast("Error: " + task.getException().getMessage(), getActivity());
                            }

                        }
                    }
                });
    }







    public void showlogoutScreen(String s){
        logoutDialog = new Dialog(getActivity());

        Objects.requireNonNull(logoutDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        logoutDialog.setContentView(R.layout.logout_box);

        WindowManager.LayoutParams par = new WindowManager.LayoutParams();
        par.copyFrom(logoutDialog.getWindow().getAttributes());
        par.width = WindowManager.LayoutParams.MATCH_PARENT;
        par.height = WindowManager.LayoutParams.WRAP_CONTENT;;
        logoutDialog.getWindow().setAttributes(par);
        logoutDialog.setCancelable(false);
        logoutDialog.setCanceledOnTouchOutside(false);
        Button btn = (Button) logoutDialog.findViewById(R.id.msg_box_btn);
        Button btn2 = (Button) logoutDialog.findViewById(R.id.logout_box_btn);
        TextView text = logoutDialog.findViewById(R.id.msg_box_msg);
        text.setText(s);

        btn.setOnClickListener(v -> {
            logoutDialog.dismiss();
        });

        btn2.setOnClickListener(v -> {
            logoutDialog.dismiss();
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            FragmentActivity a = getActivity();
            a.finish();
        });

        logoutDialog.show();

    }
}