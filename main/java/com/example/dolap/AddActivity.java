package com.example.dolap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    EditText text_name;
    EditText text_type;
    EditText text_date;
    EditText text_location;
    Button add;

    DatabaseHelper databaseHelper;
    RecyclerView.LayoutManager layoutManager;
    List<Clothes> clothesList;
    RecyclerView recyclerView;
    ActivityCheckboxAdapter activityCheckboxAdapter;

    List<Integer> checkedlistsget;
    List<Integer> checkedlist;

    List<Integer> setCheckeds;
    List<Integer> getCheckeds;

    Activity activity;

    int activity_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        activity_id = getIntent().getIntExtra("activity_id",-1);
        defineVariables();
        listeners();
        checkUpdate();
    }

    private void checkUpdate() {
        if (activity_id != -1){
            setCheckeds = new ArrayList<>();
            getCheckeds = new ArrayList<>();
            List<Activity> activity = databaseHelper.getOneActivities( activity_id );

            add.setText("Update Activity");

            text_name.setText(activity.get(0).getName());
            text_type.setText(activity.get(0).getType());
            text_date.setText(activity.get(0).getDate());
            text_location.setText(activity.get(0).getLocation());

            String clothes = activity.get(0).getClothes();
            String[] separated = clothes.split("-");

            for (String item : separated) {
                setCheckeds.add(Integer.parseInt(item));
            }
            activityCheckboxAdapter.setChecks(setCheckeds);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (NullControl(setCheckeds.size())) {
                        getCheckeds = activityCheckboxAdapter.getChecks();
                        StringBuilder clothes_ids = new StringBuilder();

                        for (int item: getCheckeds){
                            clothes_ids.append(item);
                            clothes_ids.append("-");
                        }

                        databaseHelper.updateActivity(activity_id, text_name.getText().toString(), text_type.getText().toString(), text_date.getText().toString(), text_location.getText().toString(), clothes_ids.toString());
                        Toast.makeText(getApplicationContext(), "Activity Update Success ", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(v.getContext(), EtkinlikActivity.class);
                        v.getContext().startActivity(intent);
                    }
                    else {
                        Toast.makeText( getApplicationContext(), "Error ! Please check the form ", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void listeners() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NullControl(checkedlistsget.size())){
                    activity.setName(text_name.getText().toString());
                    activity.setType(text_type.getText().toString());
                    activity.setDate(text_date.getText().toString());
                    activity.setLocation(text_location.getText().toString());

                    StringBuilder clothes_ids = new StringBuilder();

                    for (int item: checkedlistsget){
                        clothes_ids.append(item);
                        clothes_ids.append("-");
                    }

                    activity.setClothes(clothes_ids.toString());

                    databaseHelper.addActivity(activity);

                    Toast.makeText(getApplicationContext(), "Activity Added ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddActivity.this, EtkinlikActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error ! Please check the form ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean NullControl(int size) {
        return !text_name.getText().toString().equals("") && !text_type.getText().toString().equals("")  && !text_date.getText().toString().equals("")  &&
                !text_location.getText().toString().equals("")  && size != 0;
    }

    private void defineVariables() {
        text_name = (EditText) findViewById(R.id.text_name);
        text_type = (EditText) findViewById(R.id.text_type);
        text_date = (EditText) findViewById(R.id.text_date);
        text_location = (EditText) findViewById(R.id.text_location);
        add = (Button) findViewById(R.id.button);

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);

        activity = new Activity();

        checkedlist = new ArrayList<>();
        clothesList = new ArrayList<>();

        databaseHelper = new DatabaseHelper(AddActivity.this);
        activityCheckboxAdapter = new ActivityCheckboxAdapter(checkedlist, clothesList);
        checkedlistsget = activityCheckboxAdapter.getChecks();

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(activityCheckboxAdapter);

        getDataFromSQLite();
    }

    private void getDataFromSQLite() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                clothesList.clear();
                clothesList.addAll(databaseHelper.getAllClothes());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                activityCheckboxAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
