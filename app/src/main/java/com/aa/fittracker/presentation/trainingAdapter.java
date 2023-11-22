package com.aa.fittracker.presentation;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.fittracker.R;
import com.aa.fittracker.models.Training;

import java.sql.Array;
import java.util.List;

public class trainingAdapter extends RecyclerView.Adapter<trainingAdapter.MyViewHolder> {

    private List<Training> dataList;
    private Context context;

    // Constructor to initialize with data list and context
    public trainingAdapter(List<Training> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.training_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int itemsPerPage = 3;
        int startIndex = position * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, dataList.size());


        // Access individual views within the custom layout
        TextView tv1 = holder.itemView.findViewById(R.id.targetTv1);
        TextView tv2 = holder.itemView.findViewById(R.id.targetTv2);
        TextView tv3 = holder.itemView.findViewById(R.id.targetTv3);
        TextView[] tv = new TextView[]{tv1,tv2,tv3};

        for(TextView x : tv){
            x.setVisibility(View.GONE);
        }
        for (int i = startIndex; i < endIndex; i++) {
            Training currentItem = dataList.get(i);
            switch (i - startIndex){
                case 0:
                    tv1.setText(currentItem.getName());
                    tv1.setVisibility(View.VISIBLE);
                    tv1.setBackgroundColor(getColor(currentItem.getDifficulty()));
                    break;
                case 1:
                    tv2.setText(currentItem.getName());
                    tv2.setVisibility(View.VISIBLE);
                    tv2.setBackgroundColor(getColor(currentItem.getDifficulty()));
                    break;
                case 2:
                    tv3.setText(currentItem.getName());
                    tv3.setVisibility(View.VISIBLE);
                    tv3.setBackgroundColor(getColor(currentItem.getDifficulty()));
                    break;
            }
        }
    }
    public int getColor(int difficulty){
        switch (difficulty){
            case 1:
                return Color.GREEN;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.RED;
            default:
                return Color.WHITE;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size(); // Return the size of the data list
    }

    // ViewHolder class
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

