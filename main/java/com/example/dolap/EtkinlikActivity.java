package com.example.dolap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EtkinlikActivity extends AppCompatActivity {

    Button add_activity;

    DatabaseHelper databaseHelper;
    RecyclerView.LayoutManager layoutManager;
    List<Activity> activities;
    RecyclerView recyclerView;
    ActivityAdapter activityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etkinlik);
        defineVariables();
        defineListeners();
    }

    private void defineListeners() {
        add_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void defineVariables() {
        add_activity = (Button) findViewById(R.id.add_activity);

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);

        activities = new ArrayList<>();
        databaseHelper = new DatabaseHelper(EtkinlikActivity.this);
        activityAdapter = new ActivityAdapter(activities);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(activityAdapter);

        getDataFromSQLite();
    }

    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                activities.clear();
                activities.addAll(databaseHelper.getAllActivities());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                activityAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

}
