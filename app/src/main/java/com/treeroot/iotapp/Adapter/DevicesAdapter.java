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
import com.treeroot.iotapp.Model.DeviceObjects;
import com.treeroot.iotapp.Model.Devices;
import com.treeroot.iotapp.R;
import com.treeroot.iotapp.Ui.Activity.DeviceDetailActivity;


public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {

    Devices data;
    Context context;

    public DevicesAdapter(Devices data, Context context) {
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

        DeviceObjects item = data.getObjects().get(position);

        holder.tv_chip.setText(item.getName());

        holder.tv_chip.setOnClickListener(view -> {
            String getStatus = String.valueOf(item.isActive());
            Intent i=new Intent(context,DeviceDetailActivity.class);
            i.putExtra("id", item.getId());
            i.putExtra("name", item.getName());
            i.putExtra("userId", item.getUserId());
            i.putExtra("created", item.getCreated());
            i.putExtra("updated", item.getUpdated());
            i.putExtra("type", item.getType());
            i.putExtra("charge", item.getCharge());
            i.putExtra("signal", item.getSignal());
            i.putExtra("data", item.getData());
            i.putExtra("roomId", item.getRoomId());
            i.putExtra("status", item.getStatus());
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
