package com.aa.fittracker.presentation;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.trainingservice.onItemClickListener;

import java.util.List;

public class trainingAdapter extends RecyclerView.Adapter<trainingAdapter.MyViewHolder> {

    private List<Training> dataList;
    private Context context;
    private onItemClickListener listener;

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

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

        TextView tv1 = holder.itemView.findViewById(R.id.targetTv1);
        TextView tv2 = holder.itemView.findViewById(R.id.targetTv2);
        TextView tv3 = holder.itemView.findViewById(R.id.targetTv3);
        TextView[] tv = new TextView[]{tv1, tv2, tv3};

        for (TextView x : tv) {
            x.setVisibility(View.GONE);
        }
        for (int i = startIndex; i < endIndex; i++) {
            Training currentItem = dataList.get(i);
            switch (i - startIndex) {
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
        for(TextView x : tv){
            x.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    store.setTrainingInFocusName(x.getText().toString());
                    Log.i("focus: ", store.getTrainingInFocusName());
                    store.setTrainingInFocus(store.findInFocus(store.getTrainingInFocusName()));
                    Log.i("Focused",store.getTrainingInFocus().toString());
                    if(listener!=null){
                        listener.onTrainingFocus(store.getTrainingInFocus());
                    }
                }
            });
        }
    }

    public int getColor(int difficulty) {
        switch (difficulty) {
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
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
