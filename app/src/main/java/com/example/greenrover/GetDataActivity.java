package com.example.greenrover;

import static java.text.DateFormat.getDateInstance;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greenrover.data.UploadData;
import com.example.greenrover.data.User;
import com.example.greenrover.data.UserSubmission;
import com.example.greenrover.networkTasks.GetLsoa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class GetDataActivity extends AppCompatActivity implements GetLsoa.AsyncResponse {

    ImageButton backBtn;
    Button continueBtn;
    EditText T1, T2, numOfResidents;
    msgPopup d;
    Dialog loader;
    LinearLayout T1box, T2box, numOfResidentsBox;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    String postcode, lsoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        database.getReference("users")
                .child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            postcode = user.Postcode;
                            GetLsoa getLsoaTask = new GetLsoa(new GetLsoa.AsyncResponse() {
                                @Override
                                public void processFinish(String result) {
                                    lsoa = result;
                                }
                            });

                            getLsoaTask.execute(postcode);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        backBtn = findViewById(R.id.databackbtn);
        continueBtn = findViewById(R.id.addweightbtn);
        T1 = findViewById(R.id.weightT1add);
        T1box = findViewById(R.id.weightT1);
        T2 = findViewById(R.id.weightT2add);
        T2box = findViewById(R.id.weightT2);
        numOfResidents = findViewById(R.id.peopleadd);
        numOfResidentsBox = findViewById(R.id.people);
        d = new msgPopup();


        backBtn.setOnClickListener(v -> {
            finish();
        });

        continueBtn.setOnClickListener(v -> checkData());

        T1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newT1 = s.toString();
                if (!newT1.equals(T1.toString())) {
                    T1box.setBackgroundResource(R.drawable.border);
                }
            }
        });

        T2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newT2 = s.toString();
                if (!newT2.equals(T2.toString())) {
                    T2box.setBackgroundResource(R.drawable.border);
                }
            }
        });

        numOfResidents.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newNum = s.toString();
                if (!newNum.equals(numOfResidents.toString())) {
                    numOfResidentsBox.setBackgroundResource(R.drawable.border);
                }
            }
        });



    }

    @Override
    public void processFinish(String result) {
        lsoa = result;
    }

    private void checkData() {
        String t1 = T1.getText().toString();
        String t2 = T2.getText().toString();
        String num = numOfResidents.getText().toString();

        if (t1.isEmpty() && t2.isEmpty() && num.isEmpty()) {
            d.showToast("Please fill all fields", this);
            T1.requestFocus();
            T1.setError("Weight is required");
            T1box.setBackgroundResource(R.drawable.error_border);
            T2.requestFocus();
            T2.setError("Weight is required");
            T2box.setBackgroundResource(R.drawable.error_border);
            numOfResidents.requestFocus();
            numOfResidents.setError("Number of residents is required");
            numOfResidentsBox.setBackgroundResource(R.drawable.error_border);
            return;

        }else if (t1.isEmpty()) {
            d.showToast("Please enter the weight of the first pile with Type 1,2 and 5", this);
            T1.requestFocus();
            T1.setError("Weight is required");
            T1box.setBackgroundResource(R.drawable.error_border);
            return;

        }else if (t2.isEmpty()) {
            d.showToast("Please enter the weight of the second pile with Type 3,4 6, and 7", this);
            T2.requestFocus();
            T2.setError("Weight is required");
            T2box.setBackgroundResource(R.drawable.error_border);
            return;

        }else if (num.isEmpty()) {
            d.showToast("Please enter the number of residents", this);
            numOfResidents.requestFocus();
            numOfResidents.setError("Number of residents is required");
            numOfResidentsBox.setBackgroundResource(R.drawable.error_border);
            return;
        }

        int intNum = Integer.parseInt(num);
        if (intNum <= 0) {
            d.showToast("Number of residents cannot be less than 1", this);
            numOfResidents.requestFocus();
            numOfResidents.setError("Number of residents is required");
            numOfResidentsBox.setBackgroundResource(R.drawable.error_border);
            return;
        }

        AddData();

    }

    private void AddData() {
        showloadingScreen("Adding data...");
        String t1 = T1.getText().toString();
        String t2 = T2.getText().toString();
        String num = numOfResidents.getText().toString();
        int intNum = Integer.parseInt(num);
        float intT1 = Float.parseFloat(t1);
        float intT2 = Float.parseFloat(t2);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);

        UploadData uploadData = new UploadData(intT1, intT2, intNum, formattedDate,lsoa);
        UserSubmission userSubmission = new UserSubmission(mAuth.getCurrentUser().getUid(), uploadData);

        database.getReference("RecycleData").push().setValue(userSubmission);
        loader.dismiss();

        showMainActivity();

    }

    private void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    public void showloadingScreen(String text){
        loader = new Dialog(GetDataActivity.this);

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
