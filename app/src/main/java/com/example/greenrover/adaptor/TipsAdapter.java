package com.example.greenrover.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenrover.R;

import java.util.ArrayList;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipsViewHolder>{

    Context context;
    ArrayList<String> arrayList;

    public TipsAdapter(ArrayList<String> arrayList, Context context) {
        this.context = context;
        this.arrayList = arrayList;

    }

    public static class TipsViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTip;

        public TipsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTip = itemView.findViewById(R.id.Tipstext);
        }
    }

    @NonNull
    @Override
    public TipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tips_list,parent,false);

        return new TipsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipsViewHolder holder, int position) {
        String tip = arrayList.get(position);
        holder.textViewTip.setText(tip);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
