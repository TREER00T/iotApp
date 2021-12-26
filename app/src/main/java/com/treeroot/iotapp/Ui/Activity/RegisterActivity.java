package com.treeroot.iotapp.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.treeroot.iotapp.Interface.Api;
import com.treeroot.iotapp.Model.Authentication;
import com.treeroot.iotapp.R;
import com.treeroot.iotapp.Utils.ApiService;
import com.treeroot.iotapp.Utils.Link;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    Api req;
    EditText edt_email, edt_password, edt_phone, edt_username;
    AppCompatButton sing_up;
    SharedPreferences shPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        onclick();

    }

    public void init() {
        edt_email = findViewById(R.id.email);
        edt_password = findViewById(R.id.password);
        edt_phone = findViewById(R.id.phone_number);
        edt_username = findViewById(R.id.username);
        sing_up = findViewById(R.id.sing_up);
        findViewById(R.id.back).setOnClickListener(view -> finish());
        req = ApiService.getApiClient();
        shPref = getSharedPreferences(Link.SharePref, Context.MODE_PRIVATE);
    }


    public void onclick() {

        sing_up.setOnClickListener(view -> {
            ProgressDialog pd = new ProgressDialog(this);
            pd.show();
            pd.setContentView(R.layout.progressbar);
            pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            pd.setCancelable(true);
            pd.setCanceledOnTouchOutside(false);
            String getEmail = edt_email.getText().toString();
            String getPassword = edt_password.getText().toString();
            String getPhone = edt_phone.getText().toString();
            String getUsername = edt_username.getText().toString();

            if (!getEmail.isEmpty() && !getPassword.isEmpty() && !getPhone.isEmpty() && !getUsername.isEmpty()) {
                Call<Authentication> call = req.register(new Authentication(getEmail, getPassword, getPhone, getUsername));
                call.enqueue(new Callback<Authentication>() {
                    @Override
                    public void onResponse(@NonNull Call<Authentication> call, @NonNull Response<Authentication> response) {

                        if (response.code() == 201) {
                            SharedPreferences.Editor edit = getSharedPreferences(Link.SharePref, MODE_PRIVATE).edit();
                            edit.putString(Link.password, getPassword);
                            edit.putString(Link.username, getUsername);
                            edit.putString(Link.id, response.body().getId());
                            edit.putString(Link.token, response.body().getToken());
                            edit.apply();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                            LoginActivity.activity.finish();
                            pd.dismiss();
                        }
                        if (response.code() == 400 || response.code() == 404) {
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, R.string.invalid_data, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<Authentication> call, @NonNull Throwable t) {
                    }
                });
            }
            else{
                Toast.makeText(RegisterActivity.this, R.string.error_empty, Toast.LENGTH_SHORT).show();
            }
        });

    }
}