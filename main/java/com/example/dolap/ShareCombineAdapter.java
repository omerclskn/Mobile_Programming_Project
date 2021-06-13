package com.example.dolap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ShareCombineAdapter extends RecyclerView.Adapter<ShareCombineAdapter.MyViewHolder> {

    List<Combine> combines;

    public ShareCombineAdapter(List<Combine> combines) {
        this.combines = combines;
    }

    @NonNull
    @Override
    public ShareCombineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_card,parent,false);
        return new ShareCombineAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareCombineAdapter.MyViewHolder holder, int position) {
        holder.text_combine.setText("Combine " + combines.get(position).getCombineid());

        holder.clothes.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ListClothes.class);
                intent.putExtra("combine_id", combines.get(position).getCombineid());
                v.getContext().startActivity(intent);
            }
        });

        holder.share.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShareCombineIntent.class);
                intent.putExtra("combine_id", combines.get(position).getCombineid());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return combines.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_combine;
        Button share;
        Button clothes;

        public MyViewHolder(View itemView){
            super(itemView);
            text_combine = itemView.findViewById(R.id.text_combine);
            share = itemView.findViewById(R.id.share);
            clothes = itemView.findViewById(R.id.clothes);
        }

    }

}
