package com.example.name_jhondoe_1234.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.name_jhondoe_1234.Adapter.RoadWorksAdapter;
import com.example.name_jhondoe_1234.Model.DataItem;
import com.example.name_jhondoe_1234.R;
import com.example.name_jhondoe_1234.XmlPullParserHandler;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private CardView toolBar,plannerBtn;

    private static RoadWorksAdapter mAdapter;
    private static RecyclerView recyclerView;
    private static ArrayList<DataItem> dataItemArrayList;
    private ArrayList<DataItem> tempList;
    private ProgressBar progressBar;
    private TextView noDataFound;
    private ImageView filtersBtn;
    private EditText nameInput;
    private LinearLayout dateLayout;
    private TextView dateTv;
    private ImageView crossIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(MainActivity.this);
        initAll();

//        parserXMLData();

        MyAsyncTasks downloadingTask = new MyAsyncTasks();
        downloadingTask.execute();

        searchFunc();

        filtersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseData();
            }
        });
        plannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PlannerActivity.class));
            }
        });

        crossIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateLayout.setVisibility(View.GONE);

                if (dataItemArrayList.size() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noDataFound.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                }

                mAdapter = new RoadWorksAdapter(dataItemArrayList, MainActivity.this);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toast.makeText(this, "function", Toast.LENGTH_SHORT).show();
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setVisibility(View.GONE);
            MyAsyncTasks downloadingTask = new MyAsyncTasks();
            downloadingTask.execute();
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setVisibility(View.GONE);
            MyAsyncTasks downloadingTask = new MyAsyncTasks();
            downloadingTask.execute();
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }


    public class MyAsyncTasks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            // implement API in background and store the response in current variable
            String current = "";
            try {
                URL url = new URL("http://m.highwaysengland.co.uk/feeds/rss/AllEvents.xml");
                URLConnection conn = url.openConnection();
                XmlPullParserHandler parser = new XmlPullParserHandler();
                InputStream is = conn.getInputStream();
                dataItemArrayList = (ArrayList<DataItem>) parser.parse(is);
                Log.d("arraySize", dataItemArrayList.size() + "");
            } catch (Exception ex) {
                Log.d("errorinconvertings", ex.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("data", s.toString());
            // dismiss the progress dialog after receiving data from API
            progressBar.setVisibility(View.GONE);
            if (dataItemArrayList.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                noDataFound.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                noDataFound.setVisibility(View.VISIBLE);
            }

            Paper.book().write("datalist",dataItemArrayList);
            mAdapter = new RoadWorksAdapter(dataItemArrayList, MainActivity.this);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

    }

    public void chooseData() {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                dateLayout.setVisibility(View.VISIBLE);

                ArrayList<DataItem> clone = new ArrayList<>();
                for (DataItem element : dataItemArrayList) {
                    if (element.getPubDate().contains(dateString)) {
                        clone.add(element);
                    }
                }
                if (clone.size() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noDataFound.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                }

                mAdapter = new RoadWorksAdapter(clone, MainActivity.this);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();


            }
        }, yy, mm, dd);
//        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePicker.show();
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private void searchFunc() {
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    if (dataItemArrayList.size() != 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noDataFound.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noDataFound.setVisibility(View.VISIBLE);
                    }

                    mAdapter = new RoadWorksAdapter(dataItemArrayList, MainActivity.this);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    ArrayList<DataItem> clone = new ArrayList<>();
                    for (DataItem element : dataItemArrayList) {
                        if (element.getRoad().toLowerCase().contains(s.toString().toLowerCase())) {
                            clone.add(element);
                        }
                    }
                    if (clone.size() != 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noDataFound.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noDataFound.setVisibility(View.VISIBLE);
                    }

                    mAdapter = new RoadWorksAdapter(clone, MainActivity.this);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

//    public void parserXMLData() {
//        progressBar.setVisibility(View.VISIBLE);
//        try {
//            URL url = new URL("http://m.highwaysengland.co.uk/feeds/rss/AllEvents.xml");
//            URLConnection conn = url.openConnection();
//            XmlPullParserHandler parser = new XmlPullParserHandler();
//            InputStream is=conn.getInputStream();
////            InputStream is=getAssets().open("roadworks_data.xml");
//            dataItemArrayList = (ArrayList<DataItem>) parser.parse(is);
//            Log.d("arraySize", dataItemArrayList.size()+"");
//            progressBar.setVisibility(View.GONE);
//            mAdapter = new RoadWorksAdapter(dataItemArrayList, MainActivity.this);
//            recyclerView.setNestedScrollingEnabled(false);
//            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//            recyclerView.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d("arraySize",e.toString());
//        }
//    }

    private void initAll() {
        toolBar = findViewById(R.id.toolbar);
        plannerBtn = findViewById(R.id.plannerBtn);
        toolBar.setBackgroundResource(R.drawable.bottom_corer_round);

        dataItemArrayList = new ArrayList<>();
        tempList = new ArrayList<DataItem>();
        recyclerView = findViewById(R.id.product_list);
        progressBar = findViewById(R.id.spin_progress_bar);
        noDataFound = findViewById(R.id.no_product);
        nameInput = findViewById(R.id.name_input);
        filtersBtn = findViewById(R.id.filters_btn);

        dateLayout = findViewById(R.id.selected_date_layout);
        dateTv = findViewById(R.id.selected_date);
        crossIv = findViewById(R.id.cross_btn);
    }
}