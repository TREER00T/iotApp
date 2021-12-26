package com.treeroot.iotapp.Ui.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.treeroot.iotapp.Interface.Api;
import com.treeroot.iotapp.Model.AddData;
import com.treeroot.iotapp.R;
import com.treeroot.iotapp.Utils.ApiService;
import com.treeroot.iotapp.Utils.Link;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceDetailActivity extends AppCompatActivity {

    String id, name, userId, created, updated, isActive, status, type, charge, signal, data, roomId, projectId;
    TextView tv_created, tv_updated, tv_name, tv_status, tv_type, tv_signal, tv_data, tv_charge, tv_project_id, tv_room_id, tv_device_id;
    View view;
    Bundle bundle;
    ImageView edit,delete;
    Api req;
    SharedPreferences shPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        init();
        onclick();
    }


    private void onclick() {
        findViewById(R.id.back).setOnClickListener(view -> finish());
        edit.setOnClickListener(view -> {
            Intent i = new Intent(DeviceDetailActivity.this, UpdateDeviceActivity.class);
            i.putExtra("id", id);
            i.putExtra("charge", charge);
            i.putExtra("roomId", roomId);
            i.putExtra("projectId", projectId);
            i.putExtra("name", name);
            i.putExtra("signal", signal);
            i.putExtra("status", status);
            i.putExtra("type", type);
            startActivity(i);
        });
        delete.setOnClickListener(view -> {


            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        RequestDeleteRoom(Integer.parseInt(id));
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure to delete this device ?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        });
    }
    private void RequestDeleteRoom(int device_id) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.show();
        pd.setContentView(R.layout.progressbar);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        Call<JSONObject> call = req.deleteDevice(device_id, shPref.getString(Link.token, null));
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(@NonNull Call<JSONObject> call, @NonNull Response<JSONObject> response) {
                if (response.code() == 200) {
                    pd.dismiss();
                    Toast.makeText(DeviceDetailActivity.this, R.string.success_delete, Toast.LENGTH_SHORT).show();
                    finish();
                }

                if (response.code() == 400 || response.code() == 404) {
                    pd.dismiss();
                    Toast.makeText(DeviceDetailActivity.this,R.string.error_parsing, Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(@NonNull Call<JSONObject> call, @NonNull Throwable t) {
            }
        });

    }


    @SuppressLint("SetTextI18n")
    private void init() {

        req = ApiService.getApiClient();
        shPref = getSharedPreferences(Link.SharePref, Context.MODE_PRIVATE);
        bundle = getIntent().getExtras();
        tv_created = findViewById(R.id.created);
        tv_updated = findViewById(R.id.updated);
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.edit);
        tv_name = findViewById(R.id.name);
        edit = findViewById(R.id.edit);
        view = findViewById(R.id.active);
        tv_status = findViewById(R.id.tv_status);
        tv_type = findViewById(R.id.tv_type);
        tv_signal = findViewById(R.id.tv_signal);
        tv_data = findViewById(R.id.tv_data);
        tv_charge = findViewById(R.id.tv_charge);
        tv_project_id = findViewById(R.id.project_id);
        tv_device_id = findViewById(R.id.device_id);
        tv_room_id = findViewById(R.id.room_id);
        id = bundle.getString("id");
        projectId = bundle.getString("projectId");
        name = bundle.getString("name");
        userId = bundle.getString("userId");
        created = bundle.getString("created");
        updated = bundle.getString("updated");
        isActive = bundle.getString("isActive");
        status = bundle.getString("status");
        type = bundle.getString("type");
        charge = bundle.getString("charge");
        signal = bundle.getString("signal");
        data = bundle.getString("data");
        roomId = bundle.getString("roomId");
        tv_created.setText("Created at : " + created);
        tv_updated.setText("Updated at : " + updated);
        tv_name.setText("Name : " + name);
        tv_status.setText("Status : " + status);
        tv_type.setText("Type : " + type);
        tv_signal.setText("Signal : " + signal);
        tv_data.setText("Data : " + data);
        tv_charge.setText("Charge : " + charge);
        tv_device_id.setText("Device id : " + id);
        tv_room_id.setText("Room id : " + roomId);
        tv_project_id.setText("Project id : " + projectId);
        if (isActive.equals("true"))
            view.setBackgroundResource(R.drawable.shape_online);
        else
            view.setBackgroundResource(R.drawable.shape_offline);
    }
}