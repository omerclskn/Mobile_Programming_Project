package com.example.dolap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.MyViewHolder> {

    List<Activity> activities;

    public ActivityAdapter(List<Activity> activities) {
        this.activities = activities;
    }

    @NonNull
    @Override
    public ActivityAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card,parent,false);
        return new ActivityAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.MyViewHolder holder, int position) {
        holder.name.setText(activities.get(position).getName());
        holder.type.setText(activities.get(position).getType());
        holder.date.setText(activities.get(position).getDate());
        holder.location.setText(activities.get(position).getLocation());

        holder.delete.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alert = new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Activity")
                        .setMessage("Are you sure want to delete this activity?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHelper databaseHelper = new DatabaseHelper(v.getContext());
                                int id = activities.get(position).getActivity_id();
                                databaseHelper.deleteActivity(id);
                                activities.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(v.getContext(),"Activity Successfully Deleted",Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(v.getContext(), AddActivity.class);
                intent.putExtra("activity_id", activities.get(position).getActivity_id());
                v.getContext().startActivity(intent);
            }
        });

        holder.show.setOnClickListener(new AdapterView.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ListClothes.class);
                intent.putExtra("activity_id", activities.get(position).getActivity_id());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return activities.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView type;
        TextView date;
        TextView location;

        Button delete;
        Button update;
        Button show;

        public MyViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.text_name);
            type = (TextView) itemView.findViewById(R.id.text_type);
            date = (TextView) itemView.findViewById(R.id.text_date);
            location = (TextView) itemView.findViewById(R.id.text_location);

            delete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.update);
            show = itemView.findViewById(R.id.show);
        }

    }
}
