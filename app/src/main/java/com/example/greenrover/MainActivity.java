package com.example.greenrover;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.greenrover.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FloatingActionButton addBtn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    msgPopup d;
    Dialog logoutDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addBtn = findViewById(R.id.addBtn);
        d = new msgPopup();

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.account) {
                replaceFragment(new AccountFragment());
            } else if (itemId == R.id.history) {
                replaceFragment(new HistoryFragment());
            } else if (itemId == R.id.logout) {
                showlogoutScreen();
            }
            return true;
        });


        addBtn.setOnClickListener(v -> {
            showInfo();
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

    private void showInfo() {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("name","info");
        startActivity(intent);
    }

    public void showlogoutScreen(){
        logoutDialog = new Dialog(MainActivity.this);

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

        btn.setOnClickListener(v -> {
            logoutDialog.dismiss();
        });

        btn2.setOnClickListener(v -> {
            logoutDialog.dismiss();
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        logoutDialog.show();

    }

}