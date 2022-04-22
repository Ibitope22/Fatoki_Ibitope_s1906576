package com.example.name_jhondoe_1234.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.name_jhondoe_1234.Adapter.PlannerAdapter;
import com.example.name_jhondoe_1234.Model.Event;
import com.example.name_jhondoe_1234.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.paperdb.Paper;

public class PlannerActivity extends AppCompatActivity {
    private CardView toolBar, plannerBtn;
    private ImageView backBtn;

    private PlannerAdapter mAdapter;
    private  RecyclerView recyclerView;
    private  ArrayList<Event> dataItemArrayList;
    private TextView noDataFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        initAll();
        Paper.init(PlannerActivity.this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        plannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PlannerActivity.this, R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(PlannerActivity.this).inflate(R.layout.dialog_uninitialized, viewGroup, false);

                TextView saveBtn = dialogView.findViewById(R.id.transfer_btn);
                TextView cancelBtn = dialogView.findViewById(R.id.cancel_btn);
                TextView titleEt = dialogView.findViewById(R.id.title_et);
                TextView descEt = dialogView.findViewById(R.id.description_et);
                TextView dateTv = dialogView.findViewById(R.id.date_tv);


                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                        String title = titleEt.getText().toString().trim();
                        String desc = descEt.getText().toString().trim();
                        String date = dateTv.getText().toString().trim();
                        if (TextUtils.isEmpty(title)) {
                            titleEt.setError("Enter title");
                            titleEt.requestFocus();
                        } else if (TextUtils.isEmpty(desc)) {
                            descEt.setError("Enter description");
                            descEt.requestFocus();
                        } else if (TextUtils.isEmpty(date)) {
                            Toast.makeText(PlannerActivity.this, "Select date..", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<Event> eventArrayList = Paper.book().read("eventsList", new ArrayList<>());
                            Event event = new Event(title, desc, date);
                            eventArrayList.add(event);
                            Log.d("sizeOfList",eventArrayList.size()+"");
                            Paper.book().write("eventsList", eventArrayList);

                            if (eventArrayList.size() != 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                noDataFound.setVisibility(View.GONE);
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                noDataFound.setVisibility(View.VISIBLE);
                            }

                            mAdapter = new PlannerAdapter(eventArrayList, PlannerActivity.this);
                            recyclerView.setNestedScrollingEnabled(false);
                            recyclerView.setLayoutManager(new LinearLayoutManager(PlannerActivity.this));
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });

                dateTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calendar = Calendar.getInstance();
                        int yy = calendar.get(Calendar.YEAR);
                        int mm = calendar.get(Calendar.MONTH);
                        int dd = calendar.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePicker = new DatePickerDialog(PlannerActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String date = String.valueOf(dayOfMonth) + "." + String.valueOf(monthOfYear + 1)
                                        + "." + String.valueOf(year);
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.DAY_OF_MONTH, view.getDayOfMonth());
                                cal.set(Calendar.MONTH, view.getMonth());
                                cal.set(Calendar.YEAR, view.getYear());
                                cal.set(Calendar.HOUR_OF_DAY, 0);
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);

                                long startDateInMili = cal.getTimeInMillis();
                                Log.d("selectTimeInmili", startDateInMili + "");
                                String dateString = getDate(startDateInMili, "EE, dd MMM yyyy");

                                dateTv.setText(dateString);

                            }
                        }, yy, mm, dd);
                        datePicker.show();

                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataItemArrayList = Paper.book().read("eventsList", new ArrayList<>());
        if (dataItemArrayList.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            noDataFound.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noDataFound.setVisibility(View.VISIBLE);
        }

        mAdapter = new PlannerAdapter(dataItemArrayList, PlannerActivity.this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(PlannerActivity.this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private void initAll() {
        toolBar = findViewById(R.id.toolbar);
        plannerBtn = findViewById(R.id.planner_btn);
        toolBar.setBackgroundResource(R.drawable.bottom_corer_round);
        backBtn = findViewById(R.id.back_btn);
        noDataFound = findViewById(R.id.no_data);

        dataItemArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.product_list);
    }
}