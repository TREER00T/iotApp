package com.treeroot.iotapp.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.treeroot.iotapp.Interface.Api;
import com.treeroot.iotapp.Model.Authentication;
import com.treeroot.iotapp.R;
import com.treeroot.iotapp.Utils.ApiService;
import com.treeroot.iotapp.Utils.Link;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView dont_account;
    EditText edt_username,edt_password;
    AppCompatButton sing_in;
    Api req;
    SharedPreferences shPref;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        checkDataShPref();
        onclick();
        activity=this;

    }

    public void init() {
        dont_account = findViewById(R.id.dont_have_account);
        edt_password = findViewById(R.id.password);
        edt_username = findViewById(R.id.username);
        sing_in = findViewById(R.id.sing_in);
        req = ApiService.getApiClient();
        shPref = getSharedPreferences(Link.SharePref, Context.MODE_PRIVATE);
    }

    private void checkDataShPref() {
        if (shPref.contains(Link.token)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
    public void onclick() {
        setHighLightedText(dont_account, "Sign up here");
        dont_account.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this,RegisterActivity.class)));

        sing_in.setOnClickListener(view -> {
            ProgressDialog pd = new ProgressDialog(this);
            pd.show();
            pd.setContentView(R.layout.progressbar);
            pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            pd.setCancelable(true);
            pd.setCanceledOnTouchOutside(false);
            String getPassword = edt_password.getText().toString();
            String getUsername = edt_username.getText().toString();
            if (!getPassword.isEmpty() && !getUsername.isEmpty()) {

                Call<Authentication> call = req.login(new Authentication(getPassword, getUsername));
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
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            pd.dismiss();
                        }
                        if (response.code() == 400 || response.code() == 404) {
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, R.string.error_data_wrong, Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onFailure(@NonNull Call<Authentication> call, @NonNull Throwable t) {
                    }
                });
            }else{
                Toast.makeText(LoginActivity.this, R.string.error_empty, Toast.LENGTH_SHORT).show();
            }


        });

    }
    public void setHighLightedText(TextView tv, String textToHighlight) {
        String tvt = tv.getText().toString();
        int ofe = tvt.indexOf(textToHighlight, 0);
        Spannable wordToSpan = new SpannableString(tv.getText());
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToHighlight, ofs);
            if (ofe == -1)
                break;
            else {
                int blue = Color.parseColor("#1692FF");
                wordToSpan.setSpan(new ForegroundColorSpan(blue), ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(wordToSpan, TextView.BufferType.SPANNABLE);
            }
        }
    }
}