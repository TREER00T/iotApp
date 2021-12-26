package com.treeroot.iotapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.treeroot.iotapp.Model.Objects;
import com.treeroot.iotapp.Model.Rooms;
import com.treeroot.iotapp.R;
import com.treeroot.iotapp.Ui.Activity.RoomDetailActivity;


public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {

    Rooms data;
    Context context;

    public RoomsAdapter(Rooms data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_projects, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Objects item = data.getObjects().get(position);

        holder.tv_chip.setText(item.getName());

        holder.tv_chip.setOnClickListener(view -> {
            String getStatus = String.valueOf(item.isActive());
            Intent i=new Intent(context,RoomDetailActivity.class);

            i.putExtra("id", item.getId());
            i.putExtra("name", item.getName());
            i.putExtra("userId", item.getUserId());
            i.putExtra("created", item.getCreated());
            i.putExtra("updated", item.getUpdated());
            i.putExtra("projectId", item.getProjectId());
            i.putExtra("isActive", getStatus);
            context.startActivity(i);


        });
    }

    @Override
    public int getItemCount() {
        return data.getObjects().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Chip tv_chip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_chip = itemView.findViewById(R.id.chip_name);
        }

    }

}
