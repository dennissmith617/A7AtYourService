package com.example.a7atyourservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a7atyourservice.model.LiftInfo;

import java.text.SimpleDateFormat;
import java.util.List;


public class LiftInfoAdapter extends RecyclerView.Adapter<LiftInfoAdapter.ViewHolder> {

    private Context context;
    private List<LiftInfo> liftInfos;

    public LiftInfoAdapter(Context context, List<LiftInfo> liftInfos) {
        this.context = context;
        this.liftInfos = liftInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_life, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvName.setText(liftInfos.get(position).getLiftName());
        holder.tv_detail.setText("weight:"+liftInfos.get(position).getWeight()+
                "\nresp:"+liftInfos.get(position).getReps());
       String time =  new SimpleDateFormat("yyyy-MM-dd HH:mm").format(liftInfos.get(position).getTimestamp().getSeconds()*1000);
        holder.tv_time.setText(time);
    }

    @Override
    public int getItemCount() {
        return liftInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_detail;
        TextView tv_time;
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tv_detail = itemView.findViewById(R.id.tv_detail);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }


}
