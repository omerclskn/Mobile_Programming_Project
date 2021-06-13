package com.example.dolap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class AddClothes extends AppCompatActivity {

    String[] types = {"Select Type", "T-shirt", "Trousers", "Glasses", "Hat", "Shoes", "Sweatshirt"};
    String[] colors = {"Select Color", "Black", "White", "Red", "Green", "Blue", "Pink"};
    String[] patterns = {"Select Pattern", "Straight", "Plaid", "Striped"};

    TextView title;
    Spinner type;
    Spinner color;
    Spinner pattern;

    EditText buydate;
    EditText price;

    Button add;
    Button photo;

    ArrayAdapter<String> AdapterTypes;
    ArrayAdapter<String> AdapterColors;
    ArrayAdapter<String> AdapterPatterns;

    int drawerId;

    DatabaseHelper databaseHelper;

    int cloth_id;
    Clothes clothes;
    byte[] byteArray;
    final int CHOOSE_IMAGE_REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_clothes);
        drawerId = getIntent().getIntExtra("drawer_id", -1);

        defineVariables();
        listeners();
        Check_update();
    }

    public int spinner_text(String[] array, String text){
        for (int i = 0; i<array.length; i++) {
            if (array[i].equals(text))
                return i;
        }
        return -1;
    }

    public void Check_update(){
        cloth_id = getIntent().getIntExtra("cloth_id", -1);
        if (cloth_id != -1){
            List<Clothes> cloth = databaseHelper.getOneClothes( cloth_id );

            String selected_type = cloth.get(0).getType();
            type.setSelection(spinner_text(types,selected_type));

            String selected_color = cloth.get(0).getColor();
            color.setSelection(spinner_text(colors ,selected_color));

            String selected_pattern = cloth.get(0).getPattern();
            pattern.setSelection(spinner_text(patterns,selected_pattern));

            buydate.setText(cloth.get(0).getBuyDate());
            price.setText(cloth.get(0).getPrice());

            byteArray = cloth.get(0).getPhoto();

            add.setText("Update Clothes");

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NullControl()) {
                        databaseHelper.updateClothes(cloth_id, type.getSelectedItem().toString(), color.getSelectedItem().toString(), pattern.getSelectedItem().toString(), buydate.getText().toString(), price.getText().toString(), byteArray, drawerId);
                        Toast.makeText(getApplicationContext(), "Clothes Update Success ", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(v.getContext(), ListClothes.class);
                        intent.putExtra("drawer_id", drawerId);
                        v.getContext().startActivity(intent);
                    }
                    else {
                        Toast.makeText( getApplicationContext(), "Error ! Please check the form ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean NullControl(){
        if (!buydate.getText().toString().equals("") && !price.getText().toString().equals("") && type.getSelectedItemPosition() != 0 &&
                color.getSelectedItemPosition() != 0 && pattern.getSelectedItemPosition() != 0 && byteArray != null){
            return true;
        }
        return false;
    }

    private void listeners() {
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, CHOOSE_IMAGE_REQ_CODE);
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
                clothes.setType(type.getSelectedItem().toString());
                clothes.setColor(color.getSelectedItem().toString());
                clothes.setPattern(pattern.getSelectedItem().toString());
                clothes.setBuyDate(buydate.getText().toString());
                clothes.setPrice(price.getText().toString());
                clothes.setDrawerid(drawerId);
                clothes.setPhoto(byteArray);

                databaseHelper.addClothes(clothes);

                Toast.makeText(getApplicationContext(), "Clothes Added ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddClothes.this, ListClothes.class);
                intent.putExtra("drawer_id", drawerId);
                startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Error ! Please check the form ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_IMAGE_REQ_CODE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void defineVariables() {
        databaseHelper = new DatabaseHelper(AddClothes.this);
        clothes = new Clothes();
        title = (TextView) findViewById(R.id.title);
        buydate = (EditText) findViewById(R.id.text_date);
        price = (EditText) findViewById(R.id.text_price);

        type = (Spinner) findViewById(R.id.spinner_type);
        color = (Spinner) findViewById(R.id.spinner_color);
        pattern = (Spinner) findViewById(R.id.spinner_pattern);

        AdapterTypes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        AdapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(AdapterTypes);

        AdapterColors = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors);
        AdapterColors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        color.setAdapter(AdapterColors);

        AdapterPatterns = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, patterns);
        AdapterPatterns.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pattern.setAdapter(AdapterPatterns);

        add = (Button) findViewById(R.id.button_add);
        photo = (Button) findViewById(R.id.button_photo);
    }
}
