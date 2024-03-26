package com.example.greenrover.adaptor;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenrover.R;
import com.example.greenrover.data.UploadData;

import java.util.ArrayList;
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    Context context;
    ArrayList<UploadData> arrayList;

    public HistoryAdapter(ArrayList<UploadData> arrayList, Context context) {
        this.context = context;
        this.arrayList = arrayList;

    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewT1 ,textViewT2 , date,num;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewT1 = itemView.findViewById(R.id.weightT1);
            textViewT2 = itemView.findViewById(R.id.weightT2);
            date = itemView.findViewById(R.id.date);
            num = itemView.findViewById(R.id.numbOfRes);
        }
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_list,parent,false);
        view.setBackgroundResource(R.drawable.list_items);


        ImageView iconImageView = view.findViewById(R.id.listIcon);
        iconImageView.setColorFilter(ContextCompat.getColor(context, R.color.accent_color), PorterDuff.Mode.SRC_IN);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        UploadData data = arrayList.get(position);
        String t1 = "Type 1: " + String.valueOf((data.getT1())) + " kg";
        String t2 = "Type 2: " + String.valueOf((data.getT2())) + " kg";
        String t3 = "Date: " + data.getDate();
        String t4 = "Number of Residents: " + String.valueOf(data.getNumOfResidents());
        holder.textViewT1.setText(t1);
        holder.textViewT2.setText(t2);
        holder.date.setText(t3);
        holder.num.setText(t4);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
