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

public class KabinActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    RecyclerView.LayoutManager layoutManager;
    List<Combine> combines;
    RecyclerView recyclerView;
    CombineAdapter combineAdapter;
    Button add_combine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kabin);
        defineVariables();
        listeners();
    }

    private void listeners() {
        add_combine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddCombine.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void defineVariables() {
        recyclerView = (RecyclerView) findViewById(R.id.recylerview);

        combines = new ArrayList<>();
        databaseHelper = new DatabaseHelper(KabinActivity.this);
        combineAdapter = new CombineAdapter(combines, databaseHelper);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(combineAdapter);

        getDataFromSQLite();

        add_combine = (Button) findViewById(R.id.add_combine);
    }

    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
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
                combineAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
