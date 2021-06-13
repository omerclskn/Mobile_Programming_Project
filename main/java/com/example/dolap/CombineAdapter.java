package com.example.dolap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CombineAdapter extends RecyclerView.Adapter<CombineAdapter.MyViewHolder> {

    List<Combine> combines;
    DatabaseHelper databaseHelper;

    public CombineAdapter(List<Combine> combines, DatabaseHelper databaseHelper) {
        this.combines = combines;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public CombineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.combine_card,parent,false);
        return new CombineAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CombineAdapter.MyViewHolder holder, int position) {
        int head_id = combines.get(position).getHeadid();
        List<Clothes> head_clothes = databaseHelper.getOneClothes(head_id);
        byte[] imageArray_head = head_clothes.get(0).getPhoto();
        Bitmap bm_head = BitmapFactory.decodeByteArray(imageArray_head, 0, imageArray_head.length);
        holder.image_head.setImageBitmap(bm_head);

        int face_id = combines.get(position).getFaceid();
        List<Clothes> face_clothes = databaseHelper.getOneClothes(face_id);
        byte[] imageArray_face = face_clothes.get(0).getPhoto();
        Bitmap bm_face = BitmapFactory.decodeByteArray(imageArray_face, 0, imageArray_face.length);
        holder.image_face.setImageBitmap(bm_face);

        int top_id = combines.get(position).getTopid();
        List<Clothes> top_clothes = databaseHelper.getOneClothes(top_id);
        byte[] imageArray_top = top_clothes.get(0).getPhoto();
        Bitmap bm_top = BitmapFactory.decodeByteArray(imageArray_top, 0, imageArray_top.length);
        holder.image_top.setImageBitmap(bm_top);

        int bottom_id = combines.get(position).getBottomid();
        List<Clothes> bottom_clothes = databaseHelper.getOneClothes(bottom_id);
        byte[] imageArray_bottom = bottom_clothes.get(0).getPhoto();
        Bitmap bm_bottom = BitmapFactory.decodeByteArray(imageArray_bottom, 0, imageArray_bottom.length);
        holder.image_bottom.setImageBitmap(bm_bottom);

        int foot_id = combines.get(position).getFootid();
        List<Clothes> foot_clothes = databaseHelper.getOneClothes(foot_id);
        byte[] imageArray_foot = foot_clothes.get(0).getPhoto();
        Bitmap bm_foot = BitmapFactory.decodeByteArray(imageArray_foot, 0, imageArray_foot.length);
        holder.image_foot.setImageBitmap(bm_foot);

        holder.delete.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alert = new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Combines")
                        .setMessage("Are you sure want to delete this combine?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHelper databaseHelper = new DatabaseHelper(v.getContext());
                                int id = combines.get(position).getCombineid();
                                databaseHelper.deleteCombines(id);
                                combines.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(v.getContext(),"Combines Successfully Deleted",Toast.LENGTH_SHORT).show();
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return combines.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image_head;
        ImageView image_face;
        ImageView image_top;
        ImageView image_bottom;
        ImageView image_foot;

        Button delete;

        public MyViewHolder(View itemView){
            super(itemView);

            image_head = (ImageView) itemView.findViewById(R.id.photo_head);
            image_face = (ImageView) itemView.findViewById(R.id.photo_face);
            image_top = (ImageView) itemView.findViewById(R.id.photo_top);
            image_bottom = (ImageView) itemView.findViewById(R.id.photo_bottom);
            image_foot = (ImageView) itemView.findViewById(R.id.photo_foot);

            delete = itemView.findViewById(R.id.delete);
        }

    }
}
