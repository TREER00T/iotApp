package com.treeroot.iotapp.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.treeroot.iotapp.Adapter.SpinnerAdapter;
import com.treeroot.iotapp.Interface.Api;
import com.treeroot.iotapp.Model.AddData;
import com.treeroot.iotapp.Model.DeviceType;
import com.treeroot.iotapp.R;
import com.treeroot.iotapp.Utils.ApiService;
import com.treeroot.iotapp.Utils.Link;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDeviceActivity extends AppCompatActivity {

    SharedPreferences shPref;
    Api req;
    AppCompatButton go;
    EditText edt_charge, edt_device_name, edt_project_id, edt_room_id, edt_signal, edt_status;
    String id, name, userId, projectId, status, type, charge, signal, roomId;
    Bundle bundle;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_device);
        init();
        RequestToGetDeviceType();
        onclick();
    }

    private void RequestToGetDeviceType() {
        Call<DeviceType> call = req.getDeviceType(shPref.getString(Link.token, null));
        call.enqueue(new Callback<DeviceType>() {
            @Override
            public void onResponse(@NonNull Call<DeviceType> call, @NonNull Response<DeviceType> response) {
                if (response.code() == 200) {
                    DeviceType body = response.body();
                    SpinnerAdapter adapter = new SpinnerAdapter(UpdateDeviceActivity.this, body.getObjects());
                    spinner.setAdapter(adapter);
                }

                if (response.code() == 400 || response.code() == 404) {
                    Toast.makeText(UpdateDeviceActivity.this, R.string.error_parsing, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(@NonNull Call<DeviceType> call, @NonNull Throwable t) {
            }
        });

    }


    public void onclick() {
        findViewById(R.id.cancel).setOnClickListener(view -> finish());
        go.setOnClickListener(view -> {
            String str_charge = edt_charge.getText().toString();
            String str_device_name = edt_device_name.getText().toString();
            String str_project_id = edt_project_id.getText().toString();
            String str_room_id = edt_room_id.getText().toString();
            String str_signal = edt_signal.getText().toString();
            String str_status = edt_status.getText().toString();
            DeviceType item = (DeviceType) spinner.getSelectedItem();
            String str_type = item.getId();



            if (!str_charge.isEmpty() && !str_device_name.isEmpty() &&
                    !str_project_id.isEmpty() && !str_room_id.isEmpty()
                    && !str_signal.isEmpty() && !str_status.isEmpty() && !str_type.isEmpty()) {
                RequestUpdateDevice(str_charge, str_device_name, Integer.parseInt(str_project_id),
                        Integer.parseInt(str_room_id), str_signal, str_status, str_type);
            }else{
                Toast.makeText(UpdateDeviceActivity.this, R.string.error_empty, Toast.LENGTH_SHORT).show();
            }


        });
    }

    public void init() {

        bundle = getIntent().getExtras();
        req = ApiService.getApiClient();
        shPref = getSharedPreferences(Link.SharePref, Context.MODE_PRIVATE);

        edt_charge = findViewById(R.id.charge);
        edt_device_name = findViewById(R.id.device_name);
        edt_project_id = findViewById(R.id.project_id);
        edt_room_id = findViewById(R.id.room_id);
        edt_signal = findViewById(R.id.signal);
        edt_status = findViewById(R.id.status);
        spinner = findViewById(R.id.spinner);
        go = findViewById(R.id.go);

        id = bundle.getString("id");
        projectId = bundle.getString("projectId");
        name = bundle.getString("name");
        userId = bundle.getString("userId");
        roomId = bundle.getString("roomId");
        status = bundle.getString("status");
        type = bundle.getString("type");
        charge = bundle.getString("charge");
        signal = bundle.getString("signal");

        edt_charge.setText(charge);
        edt_device_name.setText(name);
        edt_project_id.setText(projectId);
        edt_room_id.setText(roomId);
        edt_signal.setText(signal);
        edt_status.setText(status);


    }

    private void RequestUpdateDevice(String charge, String device_name, int project_id, int room_id, String signal, String status, String type) {

        ProgressDialog pd = new ProgressDialog(this);
        pd.show();
        pd.setContentView(R.layout.progressbar);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        Call<AddData> call = req.updateDevice(Integer.parseInt(id), new AddData(charge, true, device_name, project_id, room_id, signal, status, type), shPref.getString(Link.token, null));
        call.enqueue(new Callback<AddData>() {
            @Override
            public void onResponse(@NonNull Call<AddData> call, @NonNull Response<AddData> response) {
                if (response.code() == 200) {
                    pd.dismiss();
                    Toast.makeText(UpdateDeviceActivity.this, R.string.success_updated, Toast.LENGTH_SHORT).show();
                    finish();

                }

                if (response.code() == 400 || response.code() == 404) {
                    pd.dismiss();
                    Toast.makeText(UpdateDeviceActivity.this,R.string.error_parsing, Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(@NonNull Call<AddData> call, @NonNull Throwable t) {
            }
        });
    }
}