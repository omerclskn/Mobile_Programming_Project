package com.example.dolap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShareCombineIntent extends AppCompatActivity {

    int combine_id;
    DatabaseHelper databaseHelper;
    List<Combine> combines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_intent);

        defineVariables();
        listeners();
    }

    private void listeners() {
    }

    private void defineVariables() {
        combine_id = getIntent().getIntExtra("combine_id", -1);
        System.out.println(combine_id);
        databaseHelper = new DatabaseHelper(ShareCombineIntent.this);

        combines = databaseHelper.getOneCombine(combine_id);

        String[] separated = new String [] {
                String.valueOf(combines.get(0).getHeadid()),
                String.valueOf(combines.get(0).getFaceid()),
                String.valueOf(combines.get(0).getTopid()),
                String.valueOf(combines.get(0).getBottomid()),
                String.valueOf(combines.get(0).getFootid())};

        byte[] image_head = databaseHelper.getClothPhoto(Integer.parseInt(separated[0]));
        byte[] image_face = databaseHelper.getClothPhoto(Integer.parseInt(separated[1]));
        byte[] image_top = databaseHelper.getClothPhoto(Integer.parseInt(separated[2]));
        byte[] image_bottom = databaseHelper.getClothPhoto(Integer.parseInt(separated[3]));
        byte[] image_foot = databaseHelper.getClothPhoto(Integer.parseInt(separated[4]));

        Bitmap bm_head = BitmapFactory.decodeByteArray(image_head, 0, image_head.length);
        Bitmap bm_face = BitmapFactory.decodeByteArray(image_face, 0, image_face.length);
        Bitmap bm_top = BitmapFactory.decodeByteArray(image_top, 0, image_top.length);
        Bitmap bm_bottom = BitmapFactory.decodeByteArray(image_bottom, 0, image_bottom.length);
        Bitmap bm_foot = BitmapFactory.decodeByteArray(image_foot, 0, image_foot.length);

        Uri head_photo = saveImage(bm_head, "head_photo.png");
        Uri face_photo = saveImage(bm_face, "face_photo.png");
        Uri top_photo = saveImage(bm_top, "top_photo.png");
        Uri bottom_photo = saveImage(bm_bottom, "bottom_photo.png");
        Uri foot_photo = saveImage(bm_foot, "foot_photo.png");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
        intent.setType("image/*"); /* This example is sharing jpeg images. */

        ArrayList<Uri> files = new ArrayList<Uri>();

        files.add(head_photo);
        files.add(face_photo);
        files.add(top_photo);
        files.add(bottom_photo);
        files.add(foot_photo);

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        startActivity(intent);

    }

    /*
    private Uri saveImageExternal(Bitmap image, String s) {
        //TODO - Should be processed in another thread
        Uri uri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), s);
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

     */

    /**
     * Saves the image as PNG to the app's cache directory.
     * @param image Bitmap to save.
     * @return Uri of the saved file or null
     */
    private Uri saveImage(Bitmap image, String s) {
        //TODO - Should be processed in another thread
        File imagesFolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, s);

            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName(), file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

}
