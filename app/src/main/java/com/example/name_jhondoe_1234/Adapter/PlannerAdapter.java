package com.example.name_jhondoe_1234.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.name_jhondoe_1234.Activity.EventDetailsActivity;
import com.example.name_jhondoe_1234.Activity.PlannerActivity;
import com.example.name_jhondoe_1234.Model.Event;
import com.example.name_jhondoe_1234.R;

import java.util.List;

public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.MyViewHolder>{

    List<Event> eventList;
    Activity context;

    public PlannerAdapter(List<Event> eventList, Activity context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_event_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Event item= eventList.get(position);
        Log.d("dataITem",item.toString());

        holder.date.setText(item.getDate());
        holder.title.setText(item.getTitle());
        holder.desc.setText(item.getDescription());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, EventDetailsActivity.class);
                intent.putExtra("details",item);
                context.startActivity(intent);
            }
        });

        holder.direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, EventDetailsActivity.class);
                intent.putExtra("details",item);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView  title,desc,date;
        ImageView direction;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.parent_layout);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date_tv);
            direction = itemView.findViewById(R.id.planner_img);
        }
    }
}
