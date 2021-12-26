package com.treeroot.iotapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.treeroot.iotapp.Model.DeviceType;
import com.treeroot.iotapp.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<DeviceType> {

    public SpinnerAdapter(Context context, List<DeviceType> list) {
        super(context, 0, list);
    }


    int id;
    public SpinnerAdapter(Context context, int id) {
        super(context, 0);
        this.id=id;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_row, parent, false
            );
        }

        TextView name = convertView.findViewById(R.id.type);

        DeviceType currentItem = getItem(position);


        if (currentItem != null) {
            name.setText(currentItem.getName());
        }

        return convertView;
    }
}