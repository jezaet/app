package com.example.greenrover;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.greenrover.adaptor.TipsAdapter;
import com.example.greenrover.data.Tips;
import com.example.greenrover.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;


public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    TextView disposed_total, collection_text;
    ProgressBar progressBar1, progressBar2, progressBar3;
    RecyclerView recyclerView;
    ArrayList<String> tips;
    Dialog loader;
    msgPopup d;
    Calendar calendar;
    int endOfWeekDayOfMonth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        disposed_total = rootView.findViewById(R.id.disposed_total);
        progressBar1 = rootView.findViewById(R.id.progressBar1);
        progressBar2 = rootView.findViewById(R.id.progressBar2);
        progressBar3 = rootView.findViewById(R.id.progressBar3);
        recyclerView = rootView.findViewById(R.id.tipsRecyclerView);
        collection_text = rootView.findViewById(R.id.collection_text);
        d = new msgPopup();
        tips = new ArrayList<>();


        database.getReference("RecycleData")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (isAdded()) {
                            if (snapshot.exists()) {
                                double one = 0;
                                double two = 0;
                                int num = 0;
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String id = dataSnapshot.child("userID").getValue(String.class);
                                    if (id.equals(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())) {
                                        DataSnapshot dataSnapshotChild = dataSnapshot.child("data");
                                        String date = dataSnapshotChild.child("date").getValue(String.class);
                                        boolean thisweek = thisweek(date);
                                        if (thisweek) {
                                            one = one + Double.parseDouble((String.valueOf(dataSnapshotChild.child("t1").getValue())));
                                            two = two + Double.parseDouble(String.valueOf(dataSnapshotChild.child("t2").getValue()));
                                            if (Integer.parseInt(String.valueOf(dataSnapshotChild.child("numOfResidents").getValue())) > num) {
                                                num = Integer.parseInt(String.valueOf(dataSnapshotChild.child("numOfResidents").getValue()));
                                            }

                                        }

                                    }

                                }

                                double total = one + two;
                                DecimalFormat decimalFormat = new DecimalFormat("#.###");
                                String roundedNumber = decimalFormat.format(total);
                                double one_total = (num * 0.166);
                                double two_total = (num * 0.065);
                                double total_total = one_total + two_total;

                                int t1 = (int) ((one / one_total) * 100);
                                int t2 = (int) ((two / two_total) * 100);
                                int t3 = (int) ((total / total_total) * 100);

                                disposed_total.setText(roundedNumber + " kg");
                                progressBar1.setProgress(t1);
                                progressBar2.setProgress(t2);
                                progressBar3.setProgress(t3);

                                calendar.add(Calendar.DAY_OF_WEEK, -4);
                                int num2 = calendar.get(Calendar.DAY_OF_MONTH);
                                String s2 = dataToText(num2);
                                String s3 = "";
                                int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                                if(currentDayOfWeek == 1){
                                    s3 = "sunday ";
                                } else if (currentDayOfWeek == 2){
                                    s3 = "monday ";
                                } else if (currentDayOfWeek == 3){
                                    s3 = "tuesday ";
                                } else if (currentDayOfWeek == 4){
                                    s3 = "wednesday ";
                                } else if (currentDayOfWeek == 5){
                                    s3 = "thursday ";
                                } else if (currentDayOfWeek == 6){
                                    s3 = "friday ";
                                } else if (currentDayOfWeek == 7){
                                    s3 = "saturday ";
                                }
                                collection_text.setText("Next Collection:\n " + s3+ s2);


                            } else {
                                disposed_total.setText("0");
                                progressBar1.setProgress(0);
                                progressBar2.setProgress(0);
                                progressBar3.setProgress(0);
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });




        database.getReference("RecyclingFacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isAdded() && getContext() != null) {
                    tips.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        tips.add(String.valueOf(dataSnapshot.child("text").getValue()));
                    }

                    Collections.shuffle(tips);
                    while (tips.size() > 3) {
                        tips.remove(tips.size() - 1);
                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    recyclerView.setAdapter(new TipsAdapter(tips, getActivity()));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                d.showToast("An error occurred while getting data. Please try again later", getActivity());
            }
        });


        return rootView;
    }




    public void showloadingScreen(String text){
        loader = new Dialog(getActivity());

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

    public boolean thisweek(String s){

        calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        calendar.add(Calendar.DAY_OF_WEEK, Calendar.MONDAY - currentDayOfWeek);
        int startOfWeekDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int startOfWeekMonth = calendar.get(Calendar.MONTH) + 1;

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        int endOfWeekDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int endOfWeekMonth = calendar.get(Calendar.MONTH) + 1;

        String[] parts = s.split("-");

        int givenDayOfMonth = Integer.parseInt(parts[2]);
        int givenMonth = Integer.parseInt(parts[1]);

        if(givenMonth == startOfWeekMonth && givenMonth == endOfWeekMonth &&
                givenDayOfMonth >= startOfWeekDayOfMonth &&
                givenDayOfMonth <= endOfWeekDayOfMonth){
            return true;
        } else {
            return false;
        }


    }

    public String dataToText(int DayOfMonth){
        String suffix;

        if (DayOfMonth >= 11 && DayOfMonth <= 13) {
            suffix = "th";
        } else {
            int lastDigit = DayOfMonth % 10;
            switch (lastDigit) {
                case 1:
                    suffix = "st";
                    break;
                case 2:
                    suffix = "nd";
                    break;
                case 3:
                    suffix = "rd";
                    break;
                default:
                    suffix = "th";
                    break;
            }
        }

        return (DayOfMonth + suffix);


    }
}