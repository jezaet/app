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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.greenrover.adaptor.HistoryAdapter;
import com.example.greenrover.adaptor.TipsAdapter;
import com.example.greenrover.data.UploadData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class HistoryFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    TextView date , nodata;
    ImageButton Backbtn, forwardbtn;
    RecyclerView recyclerView;
    ArrayList<UploadData> historyList;
    Dialog loader;
    msgPopup d;
    Calendar calendar;
    int startOfWeekDayOfMonth, startOfWeekMonth, endOfWeekDayOfMonth, endOfWeekMonth,
            startOfWeekDayOfMonthStop, startOfWeekMonthStop, endOfWeekDayOfMonthStop,
            endOfWeekMonthStop ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        showloadingScreen("Loading data...");
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        date = rootView.findViewById(R.id.textViewDate);
        Backbtn = rootView.findViewById(R.id.backDate);
        forwardbtn = rootView.findViewById(R.id.forDate);
        nodata = rootView.findViewById(R.id.NoDataTextView);
        recyclerView = rootView.findViewById(R.id.historyRecyclerView);
        d = new msgPopup();
        historyList = new ArrayList<>();

        GetThisWeek();

        Backbtn.setOnClickListener(v -> GetLastWeek());

        forwardbtn.setOnClickListener(v -> GetnextWeek());

        return rootView;
    }


    public void getdata(){
        historyList.clear();
        database.getReference("RecycleData")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (isAdded() && getContext() != null) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String id = dataSnapshot.child("userID").getValue(String.class);
                                    if (id.equals(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())) {
                                        DataSnapshot dataSnapshotChild = dataSnapshot.child("data");
                                        String date = dataSnapshotChild.child("date").getValue(String.class);
                                        Boolean inWeek = InSpecifiedWeek(date);
                                        if (inWeek) {
                                            historyList.add(new UploadData(
                                                    Float.parseFloat(dataSnapshotChild.child("t1").getValue().toString()),
                                                    Float.parseFloat(dataSnapshotChild.child("t2").getValue().toString()),
                                                    Integer.parseInt(dataSnapshotChild.child("numOfResidents").getValue().toString()),
                                                    date, " "));
                                        }

                                    }

                                }

                                if (!historyList.isEmpty()) {
                                    nodata.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                } else {
                                    nodata.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }

                                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                                recyclerView.setAdapter(new HistoryAdapter(historyList, getActivity()));

                            } else {
                                nodata.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);

                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        d.showToast("An error occurred while getting data. Please try again later", getActivity());
                    }

                });

        loader.dismiss();
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

    public void GetThisWeek(){

        calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        calendar.add(Calendar.DAY_OF_WEEK, Calendar.MONDAY - currentDayOfWeek);
        startOfWeekDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        startOfWeekMonth = calendar.get(Calendar.MONTH) + 1;

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        endOfWeekDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        endOfWeekMonth = calendar.get(Calendar.MONTH) + 1;

        startOfWeekDayOfMonthStop = startOfWeekDayOfMonth;
        startOfWeekMonthStop = startOfWeekMonth;
        endOfWeekDayOfMonthStop = endOfWeekDayOfMonth;
        endOfWeekMonthStop = endOfWeekMonth;

        String s = dataToText(startOfWeekDayOfMonth);
        String e = dataToText(endOfWeekDayOfMonth);
        date.setText("Monday "+s+" - Sunday "+e);
        getdata();
        forwardbtn.setVisibility(View.INVISIBLE);

    }

    public boolean InSpecifiedWeek(String s){
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

    public void GetLastWeek(){

        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysToSubtract = (currentDayOfWeek - Calendar.MONDAY + 7) % 7;

        calendar.add(Calendar.DAY_OF_WEEK, -daysToSubtract -7);
        startOfWeekDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        startOfWeekMonth = calendar.get(Calendar.MONTH) + 1;

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        endOfWeekDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        endOfWeekMonth = calendar.get(Calendar.MONTH) + 1;

        String s = dataToText(startOfWeekDayOfMonth);
        String e = dataToText(endOfWeekDayOfMonth);
        date.setText("Monday "+s+" - Sunday "+e);
        getdata();
        forwardbtn.setVisibility(View.VISIBLE);

    }

    public void GetnextWeek(){

        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysToAdd = (Calendar.MONDAY - currentDayOfWeek + 7) % 7;

        calendar.add(Calendar.DAY_OF_WEEK, daysToAdd);
        startOfWeekDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        startOfWeekMonth = calendar.get(Calendar.MONTH) + 1;

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        endOfWeekDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        endOfWeekMonth = calendar.get(Calendar.MONTH) + 1;

        String s = dataToText(startOfWeekDayOfMonth);
        String e = dataToText(endOfWeekDayOfMonth);
        date.setText("Monday "+s+" - Sunday "+e);
        getdata();

        if (startOfWeekDayOfMonthStop == startOfWeekDayOfMonth && startOfWeekMonthStop == startOfWeekMonth &&
                endOfWeekDayOfMonthStop == endOfWeekDayOfMonth && endOfWeekMonthStop == endOfWeekMonth){
            forwardbtn.setVisibility(View.INVISIBLE);
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