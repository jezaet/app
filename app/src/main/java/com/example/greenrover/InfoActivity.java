package com.example.greenrover;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    ImageButton backBtn;
    Button continueBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        backBtn = findViewById(R.id.infobackbutton);
        continueBtn = findViewById(R.id.submit_info);

        backBtn.setOnClickListener(v -> {
            finish();
        });

        continueBtn.setOnClickListener(v -> showADDActivity());




    }

    private void showADDActivity() {
        Intent intent = new Intent(this, GetDataActivity.class);
        startActivity(intent);
    }
}
