package com.example.dolap;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DrawerActivity extends AppCompatActivity {

    EditText drawer_name;
    Button add_drawer;

    DatabaseHelper databaseHelper;

    List<Drawer> drawers;

    Context context;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DrawerAdapter drawerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        defineVariables();
        listeners();
    }

    public void listeners(){
        add_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataToSQLite();
            }
        });
    }

    private void postDataToSQLite() {

        if (NullControl()) {

            Drawer temp = new Drawer();
            temp.setName(drawer_name.getText().toString());

            databaseHelper.addDrawer(temp);

            Toast.makeText(getApplicationContext(), "Drawer Success ", Toast.LENGTH_SHORT).show();
            clean();
            getDataFromSQLite();
        }
        else {
            Toast.makeText( getApplicationContext(), "Error ! Please check the form ", Toast.LENGTH_SHORT).show();
        }
    }

    private void clean() {
        drawer_name.setText("");
    }

    private boolean NullControl() {
        if (!drawer_name.getText().toString().equals(""))
            return true;
        return false;
    }

    private void defineVariables() {
        drawer_name = findViewById(R.id.drawer_name);
        add_drawer = findViewById(R.id.add_drawer);

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);

        drawers = new ArrayList<>();
        drawerAdapter = new DrawerAdapter(drawers);

        databaseHelper = new DatabaseHelper(DrawerActivity.this);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(drawerAdapter);

        getDataFromSQLite();
    }

    private void getDataFromSQLite() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                drawers.clear();
                drawers.addAll(databaseHelper.getAllDrawers());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                drawerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
