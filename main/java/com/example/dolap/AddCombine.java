package com.example.dolap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AddCombine extends AppCompatActivity {

    Spinner head;
    Spinner face;
    Spinner top;
    Spinner bottom;
    Spinner foot;
    Button add;
    ImageView image_head;
    ImageView image_face;
    ImageView image_top;
    ImageView image_bottom;
    ImageView image_foot;

    List<Clothes> clothes;

    Combine combine;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_combines);
        defineVariables();
        defineSpinners();
        listeners();
    }

    private void listeners() {
        head.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = head.getSelectedItem().toString();
                String[] split = item.split(Pattern.quote(")"));
                String item_id = split[0];

                for (Clothes cloth: clothes) {
                    if (item_id.equals(String.valueOf(cloth.getId()))){
                        byte[] imageArray = cloth.getPhoto();
                        Bitmap bm = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                        image_head.setImageBitmap(bm);
                    }
                }
                combine.setHeadid(Integer.parseInt(item_id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        face.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = face.getSelectedItem().toString();
                String[] split = item.split(Pattern.quote(")"));
                String item_id = split[0];

                for (Clothes cloth: clothes) {
                    if (item_id.equals(String.valueOf(cloth.getId()))){
                        byte[] imageArray = cloth.getPhoto();
                        Bitmap bm = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                        image_face.setImageBitmap(bm);
                    }
                }
                combine.setFaceid(Integer.parseInt(item_id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        top.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = top.getSelectedItem().toString();
                String[] split = item.split(Pattern.quote(")"));
                String item_id = split[0];

                for (Clothes cloth: clothes) {
                    if (item_id.equals(String.valueOf(cloth.getId()))){
                        byte[] imageArray = cloth.getPhoto();
                        Bitmap bm = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                        image_top.setImageBitmap(bm);
                    }
                }
                combine.setTopid(Integer.parseInt(item_id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bottom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = bottom.getSelectedItem().toString();
                String[] split = item.split(Pattern.quote(")"));
                String item_id = split[0];

                for (Clothes cloth: clothes) {
                    if (item_id.equals(String.valueOf(cloth.getId()))){
                        byte[] imageArray = cloth.getPhoto();
                        Bitmap bm = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                        image_bottom.setImageBitmap(bm);
                    }
                }
                combine.setBottomid(Integer.parseInt(item_id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        foot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = foot.getSelectedItem().toString();
                String[] split = item.split(Pattern.quote(")"));
                String item_id = split[0];

                for (Clothes cloth: clothes) {
                    if (item_id.equals(String.valueOf(cloth.getId()))){
                        byte[] imageArray = cloth.getPhoto();
                        Bitmap bm = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                        image_foot.setImageBitmap(bm);
                    }
                }
                combine.setFootid(Integer.parseInt(item_id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataToSQLite();
            }
        });

    }

    private void postDataToSQLite() {

        if (NullControl()) {

            databaseHelper.addCombines(combine);

            Toast.makeText(getApplicationContext(), "Combines Added ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddCombine.this, KabinActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Error ! Please check the form ", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean NullControl(){
        return head.getAdapter().getCount() != 0 && face.getAdapter().getCount() != 0 && top.getAdapter().getCount() != 0 &&
                bottom.getAdapter().getCount() != 0 && foot.getAdapter().getCount() != 0;
    }

    private void defineVariables() {
        combine = new Combine();

        head = (Spinner) findViewById(R.id.spinner_head);
        face = (Spinner) findViewById(R.id.spinner_face);
        top = (Spinner) findViewById(R.id.spinner_top);
        bottom = (Spinner) findViewById(R.id.spinner_bottom);
        foot = (Spinner) findViewById(R.id.spinner_foot);

        image_head = (ImageView) findViewById(R.id.image_head);
        image_face = (ImageView) findViewById(R.id.image_face);
        image_top = (ImageView) findViewById(R.id.image_top);
        image_bottom = (ImageView) findViewById(R.id.image_bottom);
        image_foot = (ImageView) findViewById(R.id.image_foot);

        add = (Button) findViewById(R.id.button_add);

        clothes = new ArrayList<>();
        databaseHelper = new DatabaseHelper(AddCombine.this);
        clothes = databaseHelper.getAllClothes();
    }

    private void defineSpinners() {
        ArrayList<String> head_clothes = new ArrayList<>();
        ArrayList<String> face_clothes = new ArrayList<>();
        ArrayList<String> top_clothes = new ArrayList<>();
        ArrayList<String> bottom_clothes = new ArrayList<>();
        ArrayList<String> foot_clothes = new ArrayList<>();

        for (Clothes cloth: clothes) {
            switch (cloth.getType()) {
                case "T-shirt":
                case "Sweatshirt":
                    top_clothes.add(cloth.getId() + ") " + cloth.getColor() + cloth.getPattern() + cloth.getType());
                    break;
                case "Hat":
                    head_clothes.add(cloth.getId() + ") " + cloth.getColor() + cloth.getPattern() + cloth.getType());
                    break;
                case "Glasses":
                    face_clothes.add(cloth.getId() + ") " + cloth.getColor() + cloth.getPattern() + cloth.getType());
                    break;
                case "Trousers":
                    bottom_clothes.add(cloth.getId() + ") " + cloth.getColor() + cloth.getPattern() + cloth.getType());
                    break;
                case "Shoes":
                    foot_clothes.add(cloth.getId() + ") " + cloth.getColor() + cloth.getPattern() + cloth.getType());
                    break;
                default:
                    break;
            }
        }

        ArrayAdapter<String> top_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, top_clothes);
        top_adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        top.setAdapter(top_adapter);

        ArrayAdapter<String> head_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, head_clothes);
        head_adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        head.setAdapter(head_adapter);

        ArrayAdapter<String> face_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, face_clothes);
        face_adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        face.setAdapter(face_adapter);

        ArrayAdapter<String> bottom_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bottom_clothes);
        bottom_adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        bottom.setAdapter(bottom_adapter);

        ArrayAdapter<String> foot_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, foot_clothes);
        foot_adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        foot.setAdapter(foot_adapter);
    }
}
