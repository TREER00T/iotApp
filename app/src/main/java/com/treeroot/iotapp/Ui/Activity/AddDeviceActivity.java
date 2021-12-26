package com.treeroot.iotapp.Ui.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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

public class AddDeviceActivity extends AppCompatActivity {

    Api req;
    SharedPreferences shPref;
    EditText charge, device_name, project_id, room_id, signal, status;
    AppCompatButton go, cancel;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);


        init();
        RequestToGetDeviceType();
        onClick();
    }


    private void onClick() {

        cancel.setOnClickListener(view -> finish());
        go.setOnClickListener(view -> {

            String str_charge = charge.getText().toString();
            String str_device_name = device_name.getText().toString();
            String str_project_id = project_id.getText().toString();
            String str_room_id = room_id.getText().toString();
            String str_signal = signal.getText().toString();
            String str_status = status.getText().toString();
            DeviceType item = (DeviceType) spinner.getSelectedItem();
            String str_type = item.getId();



            if (!str_charge.isEmpty() && !str_device_name.isEmpty() &&
                    !str_project_id.isEmpty() && !str_room_id.isEmpty()
                    && !str_signal.isEmpty() && !str_status.isEmpty() && !str_type.isEmpty()) {

                RequestAddDevice(str_charge, str_device_name, Integer.parseInt(str_project_id),
                        Integer.parseInt(str_room_id), str_signal, str_status, str_type);

            }else{
                Toast.makeText(AddDeviceActivity.this, R.string.error_empty, Toast.LENGTH_SHORT).show();
            }




        });

    }

    private void init() {

        charge = findViewById(R.id.charge);
        device_name = findViewById(R.id.device_name);
        project_id = findViewById(R.id.project_id);
        room_id = findViewById(R.id.room_id);
        signal = findViewById(R.id.signal);
        status = findViewById(R.id.status);
        go = findViewById(R.id.go);
        cancel = findViewById(R.id.cancel);
        spinner = findViewById(R.id.spinner);
        req = ApiService.getApiClient();
        shPref = getSharedPreferences(Link.SharePref, Context.MODE_PRIVATE);
    }

    private void RequestToGetDeviceType() {
        Call<DeviceType> call = req.getDeviceType(shPref.getString(Link.token, null));
        call.enqueue(new Callback<DeviceType>() {
            @Override
            public void onResponse(@NonNull Call<DeviceType> call, @NonNull Response<DeviceType> response) {
                if (response.code() == 200) {
                    DeviceType body = response.body();
                    SpinnerAdapter adapter=new SpinnerAdapter(AddDeviceActivity.this,body.getObjects());
                    spinner.setAdapter(adapter);
                }

                if (response.code() == 400 || response.code() == 404) {
                    Toast.makeText(AddDeviceActivity.this, R.string.error_parsing, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(@NonNull Call<DeviceType> call, @NonNull Throwable t) {
            }
        });

    }


    private void RequestAddDevice(String charge, String device_name, int project_id, int room_id, String signal, String status, String type) {

        ProgressDialog pd = new ProgressDialog(this);
        pd.show();
        pd.setContentView(R.layout.progressbar);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        Call<AddData> call = req.addDevice(new AddData(charge, true, device_name, project_id, room_id, signal, status, type), shPref.getString(Link.token, null));
        call.enqueue(new Callback<AddData>() {
            @Override
            public void onResponse(@NonNull Call<AddData> call, @NonNull Response<AddData> response) {
                if (response.code() == 201) {
                    pd.dismiss();
                    Toast.makeText(AddDeviceActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                    finish();

                }

                if (response.code() == 400 || response.code() == 404) {
                    pd.dismiss();
                    Toast.makeText(AddDeviceActivity.this, R.string.invalid_data, Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(@NonNull Call<AddData> call, @NonNull Throwable t) {
            }
        });
    }

}