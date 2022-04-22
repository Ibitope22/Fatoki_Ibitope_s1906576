package com.example.name_jhondoe_1234.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.name_jhondoe_1234.Model.DataItem;
import com.example.name_jhondoe_1234.R;
import com.example.name_jhondoe_1234.Activity.RoadWorksDetails;

import java.util.List;

public class RoadWorksAdapter extends RecyclerView.Adapter<RoadWorksAdapter.MyViewHolder>{

    List<DataItem> dataItemList;
    Activity context;

    public RoadWorksAdapter(List<DataItem> dataItems, Activity context) {
        this.dataItemList = dataItems;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_road_map_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DataItem item= dataItemList.get(position);
        Log.d("dataITem",item.toString());

        holder.road.setText("Road: "+item.getRoad());
        holder.region.setText("Region : "+item.getRegion());
        holder.category.setText("Category : "+item.getCategory1());
        holder.date.setText(item.getPubDate());
        holder.title.setText(item.getTitle());
        holder.author.setText("Author: "+item.getAuthor());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, RoadWorksDetails.class);
                intent.putExtra("details",item);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItemList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView  road,region,category,date,title,author;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.parent_layout);
            road = itemView.findViewById(R.id.road_tv);
            region = itemView.findViewById(R.id.region_tv);
            category = itemView.findViewById(R.id.category);
            date = itemView.findViewById(R.id.date_tv);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
        }
    }
}
