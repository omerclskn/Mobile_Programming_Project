package com.example.dolap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {

    List<Drawer> drawers;

    public DrawerAdapter(List<Drawer> drawers) {
        this.drawers = drawers;
    }

    @NonNull
    @Override
    public DrawerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawers,parent,false);
        return new DrawerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerAdapter.MyViewHolder holder, int position) {
        holder.text_drawer.setText(drawers.get(position).getName());

        holder.delete.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Drawer")
                        .setMessage("Are you sure want to delete this drawer? All clothes will be delete")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHelper databaseHelper = new DatabaseHelper(v.getContext());
                                int id = drawers.get(position).getId();
                                databaseHelper.deletedrawer(id);
                                drawers.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(v.getContext(),"Drawer Successfully Deleted",Toast.LENGTH_SHORT).show();
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        holder.clothes.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ListClothes.class);
                intent.putExtra("drawer_id", drawers.get(position).getId());
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return drawers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_drawer;
        Button delete;
        Button clothes;

        public MyViewHolder(View itemView){
            super(itemView);
            text_drawer = itemView.findViewById(R.id.text_drawer);
            delete = itemView.findViewById(R.id.delete);
            clothes = itemView.findViewById(R.id.clothes);
        }

    }
}
