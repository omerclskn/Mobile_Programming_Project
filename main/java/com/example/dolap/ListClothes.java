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

public class ListClothes extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    RecyclerView.LayoutManager layoutManager;
    List<Clothes> clothes;

    List<Activity> activity;
    List<Combine> combines;

    RecyclerView recyclerView;
    ClothesAdapter clothesAdapter;

    Button add_clothes;

    int drawer_id;
    int activity_id;
    int combine_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_clothes);
        defineVariables();
        listeners();
        checkActivity();
        checkShare();
    }

    private void checkShare() {
        if (combine_id != -1){
            add_clothes.setVisibility(View.INVISIBLE);

            combines = databaseHelper.getOneCombine(combine_id);

            String[] separated = new String [] {
                    String.valueOf(combines.get(0).getHeadid()),
                    String.valueOf(combines.get(0).getFaceid()),
                    String.valueOf(combines.get(0).getTopid()),
                    String.valueOf(combines.get(0).getBottomid()),
                    String.valueOf(combines.get(0).getFootid())};

            getDataFromSQLiteActivity(separated);

        }
    }

    private void checkActivity() {
        if (activity_id != -1){
            add_clothes.setVisibility(View.INVISIBLE);

            activity = databaseHelper.getOneActivities(activity_id);

            String activity_clothes = activity.get(0).getClothes();
            String[] separated = activity_clothes.split("-");

            getDataFromSQLiteActivity(separated);

        }
    }

    private void listeners() {
        add_clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddClothes.class);
                intent.putExtra("drawer_id", drawer_id);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void defineVariables() {
        drawer_id = getIntent().getIntExtra("drawer_id", -1);
        activity_id = getIntent().getIntExtra("activity_id",-1);
        combine_id = getIntent().getIntExtra("combine_id",-1);

        add_clothes = (Button) findViewById(R.id.add_clothes);

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);

        clothes = new ArrayList<>();

        databaseHelper = new DatabaseHelper(ListClothes.this);
        clothesAdapter = new ClothesAdapter(clothes);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(clothesAdapter);

        if (activity_id == -1) getDataFromSQLite(drawer_id);
    }

    private void getDataFromSQLite(int drawer_id) {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                clothes.clear();
                clothes.addAll(databaseHelper.getClothes(drawer_id));
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                clothesAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void getDataFromSQLiteActivity(String[] seperated) {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                clothes.clear();
                for (String item : seperated) {
                    clothes.addAll(databaseHelper.getOneClothes(Integer.parseInt(item)));
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                clothesAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

}
