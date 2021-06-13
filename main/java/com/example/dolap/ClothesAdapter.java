package com.example.dolap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClothesAdapter extends RecyclerView.Adapter<ClothesAdapter.MyViewHolder> {
    List<Clothes> clothes;

    public ClothesAdapter(List<Clothes> clothes) {
        this.clothes = clothes;
    }

    @NonNull
    @Override
    public ClothesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cloth_card,parent,false);
        return new ClothesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClothesAdapter.MyViewHolder holder, int position) {
        holder.type.setText(" Type: " + clothes.get(position).getType());
        holder.color.setText(" Color: " + clothes.get(position).getColor());
        holder.pattern.setText(" Pattern: " + clothes.get(position).getPattern());
        holder.date.setText(" Buy Date: " + clothes.get(position).getBuyDate());
        holder.price.setText(" Price: " + clothes.get(position).getPrice());
        holder.drawerid.setText(String.valueOf(clothes.get(position).getDrawerid()));

        byte[] imageArray = clothes.get(position).getPhoto();
        Bitmap bm = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
        holder.image.setImageBitmap(bm);

        holder.delete.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alert = new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Clothes")
                        .setMessage("Are you sure want to delete this clothes?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHelper databaseHelper = new DatabaseHelper(v.getContext());
                                int id = clothes.get(position).getId();
                                databaseHelper.deleteClothes(id);
                                clothes.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(v.getContext(),"Clothes Successfully Deleted",Toast.LENGTH_SHORT).show();
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        holder.update.setOnClickListener(new AdapterView.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddClothes.class);
                intent.putExtra("drawer_id", clothes.get(position).getDrawerid());
                intent.putExtra("cloth_id", clothes.get(position).getId());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return clothes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView type;
        TextView color;
        TextView pattern;
        TextView price;
        TextView date;
        TextView drawerid;
        ImageView image;
        Button delete;
        Button update;

        public MyViewHolder(View itemView){
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.text_type);
            color = (TextView) itemView.findViewById(R.id.text_color);
            pattern = (TextView) itemView.findViewById(R.id.text_pattern);
            price = (TextView) itemView.findViewById(R.id.text_price);
            date = (TextView) itemView.findViewById(R.id.text_date);
            drawerid = (TextView) itemView.findViewById(R.id.text_drawerid);

            image = (ImageView) itemView.findViewById(R.id.photo);

            delete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.update);
        }

    }
}
