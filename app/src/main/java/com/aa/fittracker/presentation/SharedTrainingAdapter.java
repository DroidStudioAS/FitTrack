package com.aa.fittracker.presentation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.SharedTraining;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.trainingservice.onItemClickListener;
import java.util.List;

public class SharedTrainingAdapter extends RecyclerView.Adapter<SharedTrainingAdapter.MyViewHolder> {

    private List<SharedTraining> dataList;

    private Context context;
    private onItemClickListener listener;

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public void setDataList(List<SharedTraining> dataList) {
        this.dataList = dataList;
    }

    public SharedTrainingAdapter(List<SharedTraining> dataList, Context context) {
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
            x.setTextColor(Color.WHITE);
            // Create a shape drawable with rounded corners and black stroke
            GradientDrawable shapeDrawable = new GradientDrawable();
            shapeDrawable.setShape(GradientDrawable.RECTANGLE);
            shapeDrawable.setCornerRadius(10); // 8dp corners
            shapeDrawable.setStroke(12, Color.BLACK); // Black stroke

            // Get the index for current TextView in the loop
            int index = startIndex + java.util.Arrays.asList(tv).indexOf(x);

            if (index < endIndex) {
                SharedTraining currentItem = dataList.get(index);
                x.setText(currentItem.getShared_training_name());
                x.setVisibility(View.VISIBLE);

                // Set the background color based on your logic (using getColor method)
                int difficulty = currentItem.getShared_training_difficulty();
                int backgroundColor = getColor(difficulty); // Use your getColor method
                shapeDrawable.setColor(backgroundColor);


                // Apply the shape drawable as the background for the TextView
                x.setBackground(shapeDrawable);

                // Set click listener
              /*  x.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Your onClick logic here
                        store.setTrainingInFocusName(x.getText().toString());
                        store.setTrainingInFocus(store.findInFocus(store.getTrainingInFocusName()));
                        if (listener != null) {
                            listener.onTrainingFocus(store.getTrainingInFocus());
                        }
                    }
                });*/
            } else {
                x.setVisibility(View.GONE);
            }
        }
    }

    public int getColor(int difficulty) {
        switch (difficulty) {
            case 1:
                return Color.parseColor("#43C3DD"); // easy_green
            case 2:
                return Color.parseColor("#FFD230"); // mid_yellow
            case 3:
                return Color.parseColor("#800080"); // hard_red
            default:
                return Color.WHITE;
        }
    }

    @Override
    public int getItemCount() {
        return (dataList.size() + 2) / 3; // Divide by 3 items per row
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
