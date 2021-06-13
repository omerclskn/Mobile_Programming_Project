package com.example.dolap;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ShareCombine extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    RecyclerView.LayoutManager layoutManager;
    List<Combine> combines;

    RecyclerView recyclerView;
    ShareCombineAdapter shareCombineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_combine);
        defineVariables();
    }

    private void defineVariables() {

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);

        combines = new ArrayList<>();
        shareCombineAdapter = new ShareCombineAdapter(combines);

        databaseHelper = new DatabaseHelper(ShareCombine.this);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(shareCombineAdapter);

        getDataFromSQLite();
    }

    private void getDataFromSQLite() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                combines.clear();
                combines.addAll(databaseHelper.getAllCombines());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                shareCombineAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

}
