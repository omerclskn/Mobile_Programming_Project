package com.example.dolap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ActivityCheckboxAdapter extends RecyclerView.Adapter<ActivityCheckboxAdapter.MyViewHolder>{

    List<Clothes> clothesList;
    List<Integer> checks;

    public ActivityCheckboxAdapter(List<Integer> checks , List<Clothes> clothesList) {
        this.checks = checks;
        this.clothesList = clothesList;
    }

    @NonNull
    @Override
    public ActivityCheckboxAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cloth_checkbox,parent,false);
        return new ActivityCheckboxAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityCheckboxAdapter.MyViewHolder holder, int position) {

        if (checks.size() != 0){
            for (int i = 0 ; i < checks.size(); i++) {
                if (checks.get(i) == clothesList.get(position).getId()) {
                    holder.checkbox.setChecked(true);
                }
            }
        }

        byte[] imageArray = clothesList.get(position).getPhoto();
        Bitmap bm = BitmapFactory.decodeByteArray(imageArray, 0 ,imageArray.length);

        holder.photo.setImageBitmap(bm);

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checks.add(clothesList.get(position).getId());
                }
                else{
                    for (int i = 0 ; i < checks.size(); i++){
                        if (checks.get(i) == clothesList.get(position).getId()){
                            checks.remove(i);
                            break;
                        }
                    }
                }
            }
        });
    }

    public List<Integer> getChecks() {
        return checks;
    }

    public void setChecks(List<Integer> checks) {
        this.checks = checks;
    }

    @Override
    public int getItemCount() {
        return clothesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        ImageView photo;

        public MyViewHolder(View itemView){
            super(itemView);

            checkbox = itemView.findViewById(R.id.checkbox);
            photo = itemView.findViewById(R.id.photo);
        }

    }
}
