package com.example.a7atyourservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a7atyourservice.model.Foods;

import java.util.ArrayList;

public class DietInfoAdapter extends RecyclerView.Adapter<DietInfoAdapter.ViewHolder>{
    private ArrayList<Foods> foodInfo;
    private Context context;

    public DietInfoAdapter(ArrayList<Foods> foodInfo, Context context){
        this.foodInfo = foodInfo;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_food,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_name.setText(foodInfo.get(position).getName());
        holder.tv_fat.setText("Fat: " + foodInfo.get(position).getFat());
        holder.tv_carb.setText("Carb: " + foodInfo.get(position).getCarb());
        holder.tv_prot.setText("Protein: " + foodInfo.get(position).getProtein());
        holder.tv_cal.setText("Calories: " + foodInfo.get(position).getCal());
    }

    @Override
    public int getItemCount() {
        return foodInfo.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_fat;
        TextView tv_carb;
        TextView tv_prot;
        TextView tv_cal;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_foodName);
            tv_fat= itemView.findViewById(R.id.tv_fat);
            tv_carb = itemView.findViewById(R.id.tv_carb);
            tv_prot= itemView.findViewById(R.id.tv_prot);
            tv_cal= itemView.findViewById(R.id.tv_cal);
        }
    }
}
