package com.treeroot.iotapp.Ui.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class RoomDetailActivity extends AppCompatActivity {

    TextView tv_created, tv_updated, tv_name, tv_project_id, tv_room_id;
    View view;
    String id, name, userId, created, updated, isActive, projectId;
    Bundle bundle;
    SharedPreferences shPref;
    Api req;
    ImageView edit, delete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        init();
        onclick();
    }

    private void onclick() {
        findViewById(R.id.back).setOnClickListener(view -> finish());
        edit.setOnClickListener(view -> {
            showProjectDialog();

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
            builder.setMessage("Are you sure to delete this room ?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        });
    }


    private void RequestDeleteRoom(int room_id) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.show();
        pd.setContentView(R.layout.progressbar);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        Call<JSONObject> call = req.deleteRoom(room_id, shPref.getString(Link.token, null));
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(@NonNull Call<JSONObject> call, @NonNull Response<JSONObject> response) {
                if (response.code() == 200) {
                    pd.dismiss();
                    Toast.makeText(RoomDetailActivity.this, R.string.success_delete, Toast.LENGTH_SHORT).show();
                    finish();
                }

                if (response.code() == 400 || response.code() == 404) {
                    pd.dismiss();
                    Toast.makeText(RoomDetailActivity.this, R.string.error_parsing, Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(@NonNull Call<JSONObject> call, @NonNull Throwable t) {
            }
        });

    }


    void showProjectDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.update_room_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        EditText edt_project = dialog.findViewById(R.id.project_name);
        AppCompatButton go = dialog.findViewById(R.id.go);
        AppCompatButton cancel = dialog.findViewById(R.id.cancel);


        go.setOnClickListener(v -> {

            String str_project = edt_project.getText().toString();


            if (!str_project.isEmpty() && str_project.length() > 3) {
                RequestUpdateProject(str_project);
                dialog.dismiss();
            }
            if (str_project.isEmpty()){
                Toast.makeText(RoomDetailActivity.this, R.string.error_empty, Toast.LENGTH_SHORT).show();

            }


        });

        cancel.setOnClickListener(v -> {

            dialog.dismiss();

        });

        dialog.show();
    }

    private void RequestUpdateProject(String project_name) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.show();
        pd.setContentView(R.layout.progressbar);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        int project_id = Integer.parseInt(projectId);
        Call<AddData> call = req.updateRoom(Integer.parseInt(id), new AddData(true, project_name, project_id), shPref.getString(Link.token, null));
        call.enqueue(new Callback<AddData>() {
            @Override
            public void onResponse(@NonNull Call<AddData> call, @NonNull Response<AddData> response) {
                if (response.code() == 200) {
                    pd.dismiss();
                    Toast.makeText(RoomDetailActivity.this, R.string.success_updated, Toast.LENGTH_SHORT).show();
                }

                if (response.code() == 400 || response.code() == 404) {
                    pd.dismiss();
                    Toast.makeText(RoomDetailActivity.this, R.string.error_parsing, Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(@NonNull Call<AddData> call, @NonNull Throwable t) {
            }
        });

    }


    @SuppressLint("SetTextI18n")
    private void init() {

        req = ApiService.getApiClient();
        shPref = getSharedPreferences(Link.SharePref, Context.MODE_PRIVATE);
        tv_created = findViewById(R.id.created);
        tv_room_id = findViewById(R.id.tv_room_id);
        tv_updated = findViewById(R.id.updated);
        tv_name = findViewById(R.id.name);
        view = findViewById(R.id.active);
        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);
        tv_project_id = findViewById(R.id.tv_project_id);
        bundle = getIntent().getExtras();
        id = bundle.getString("id");
        name = bundle.getString("name");
        userId = bundle.getString("userId");
        projectId = bundle.getString("projectId");
        created = bundle.getString("created");
        updated = bundle.getString("updated");
        isActive = bundle.getString("isActive");
        tv_created.setText("Created at : " + created);
        tv_updated.setText("Updated at : " + updated);
        tv_name.setText("Name : " + name);
        tv_project_id.setText("Project id : " + projectId);
        tv_room_id.setText("Room id : " + id);
        if (isActive.equals("true"))
            view.setBackgroundResource(R.drawable.shape_online);
        else
            view.setBackgroundResource(R.drawable.shape_offline);
    }
}