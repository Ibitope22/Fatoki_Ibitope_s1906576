package com.example.name_jhondoe_1234.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.name_jhondoe_1234.Model.DataItem;
import com.example.name_jhondoe_1234.R;

public class RoadWorksDetails extends AppCompatActivity {
    private CardView toolBar;
    private ImageView backBtn;
    DataItem item;
    private LinearLayout locationBtn;
    private TextView author,category1,category2,description,guid,link,pubDate,title,reference,road,region,county,latitude,longitude,overallStart,overallEnd,eventStart,eventEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_works_details);

        initAll();

        item= (DataItem) getIntent().getSerializableExtra("details");
        if(item!=null){
            title.setText("Title: " + item.getTitle());
            description.setText("Description: " + item.getDescription());
            guid.setText("Guid: " + item.getGuid());
            link.setText("Link: " + item.getLink());
            pubDate.setText(item.getPubDate());
            reference.setText("Reference: " + item.getReference());
            road.setText("Road: " + item.getRoad());
            region.setText("Region: " + item.getRegion());
            county.setText("County: " + item.getCounty());
            latitude.setText("Latitude: " + item.getLatitude());
            longitude.setText("Longitude: " + item.getLongitude());
            author.setText("Author: " + item.getAuthor());
            category2.setText("Category: " + item.getCategory1());
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(item.getLink()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RoadWorksDetails.this, EventLocationActivity.class);
                intent.putExtra("lat",item.getLatitude());
                intent.putExtra("long",item.getLongitude());
                startActivity(intent);
            }
        });


    }

    private void initAll() {
        toolBar = findViewById(R.id.toolbar);
        toolBar.setBackgroundResource(R.drawable.bottom_corer_round);

        author = findViewById(R.id.author);
        locationBtn = findViewById(R.id.location_btn);
        backBtn = findViewById(R.id.back_btn);
        category2 = findViewById(R.id.category);
        description = findViewById(R.id.description);
        guid = findViewById(R.id.guid);
        link = findViewById(R.id.link);
        pubDate = findViewById(R.id.pubDate);
        title = findViewById(R.id.title);
        reference = findViewById(R.id.reference);
        road = findViewById(R.id.road);
        region = findViewById(R.id.region);
        county = findViewById(R.id.county);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
    }
}