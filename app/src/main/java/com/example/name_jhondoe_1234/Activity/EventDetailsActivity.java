package com.example.name_jhondoe_1234.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.name_jhondoe_1234.Adapter.RoadWorksAdapter;
import com.example.name_jhondoe_1234.Model.DataItem;
import com.example.name_jhondoe_1234.Model.Event;
import com.example.name_jhondoe_1234.R;

import java.util.ArrayList;

import io.paperdb.Paper;

public class EventDetailsActivity extends AppCompatActivity {
    Event item;
    private CardView toolBar;

    private ImageView backBtn;
    private static RoadWorksAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<DataItem> dataItemArrayList;
    private TextView noDataFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        item= (Event) getIntent().getSerializableExtra("details");

        Paper.init(EventDetailsActivity.this);
        initAll();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initAll() {
        toolBar = findViewById(R.id.toolbar);
        toolBar.setBackgroundResource(R.drawable.bottom_corer_round);
        backBtn = findViewById(R.id.back_btn);
        noDataFound = findViewById(R.id.no_data);

        dataItemArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.product_list);
        ArrayList<DataItem> tempList= Paper.book().read("datalist",new ArrayList<>());
        int count=0;
        for(int i=0;i<tempList.size();i++){
            DataItem dateItem=tempList.get(i);
            if(dateItem.getPubDate().contains(item.getDate())){
                dataItemArrayList.add(dateItem);
            }
            count++;
        }
        Log.d("arrayListDetails",count+"");
        Log.d("arrayListDetails",tempList.size()+"");
        if(count==tempList.size()){
            if (dataItemArrayList.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                noDataFound.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                noDataFound.setVisibility(View.VISIBLE);
            }

            mAdapter = new RoadWorksAdapter(dataItemArrayList, EventDetailsActivity.this);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(EventDetailsActivity.this));
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

    }
}