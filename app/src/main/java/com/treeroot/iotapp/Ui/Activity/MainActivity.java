package com.treeroot.iotapp.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.treeroot.iotapp.Adapter.DevicesAdapter;
import com.treeroot.iotapp.Adapter.ProjectsAdapter;
import com.treeroot.iotapp.Adapter.RoomsAdapter;
import com.treeroot.iotapp.Interface.Api;
import com.treeroot.iotapp.Model.AddData;
import com.treeroot.iotapp.Model.Devices;
import com.treeroot.iotapp.Model.Projects;
import com.treeroot.iotapp.Model.Rooms;
import com.treeroot.iotapp.R;
import com.treeroot.iotapp.Utils.ApiService;
import com.treeroot.iotapp.Utils.Link;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView rec_projects, rec_rooms, rec_devices;
    LinearLayout linear_projects, linear_rooms, linear_devices, linear_lottie;
    TextView tv_firstPro;
    Api req;
    SharedPreferences shPref;
    ProjectsAdapter projectsAdapter;
    RoomsAdapter roomsAdapter;
    DevicesAdapter devicesAdapter;
    FloatingActionButton floatBtn;
    SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        onclick();
        RequestToGetProjects();
        setHighLightedText(tv_firstPro, "+");


    }


    private void onclick() {

        refresh.setOnRefreshListener(() -> {
            RequestToGetProjects();
            new Handler().postDelayed(() -> refresh.setRefreshing(false), 1000);
        });
        floatBtn.setOnClickListener(view -> {
            final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
            View bottomSheet = LayoutInflater.from(this).inflate(R.layout.bottomsheetlayout
                    , findViewById(R.id.lin));
            dialog.setContentView(bottomSheet);
            dialog.show();
            bottomSheet.findViewById(R.id.layoutProject).setOnClickListener(view1 -> {

                dialog.dismiss();
                showProjectDialog();

            });
            bottomSheet.findViewById(R.id.layoutRoom).setOnClickListener(view1 -> {
                dialog.dismiss();
                showRoomDialog();
            });
            bottomSheet.findViewById(R.id.layoutDevice).setOnClickListener(view1 -> {
                dialog.dismiss();
                CheckIsEmptyRoom();
            });
        });
    }

    private void CheckIsEmptyRoom() {
        Call<Rooms> call = req.getRooms(shPref.getString(Link.token, null));
        call.enqueue(new Callback<Rooms>() {
            @Override
            public void onResponse(@NonNull Call<Rooms> call, @NonNull Response<Rooms> response) {
                if (response.code() == 200) {
                    if (response.body().getObjects().isEmpty()) {
                        Toast.makeText(MainActivity.this, R.string.room_not_found, Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(MainActivity.this, AddDeviceActivity.class));
                    }
                }

                if (response.code() == 400 || response.code() == 404) {
                    Toast.makeText(MainActivity.this, R.string.error_parsing, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(@NonNull Call<Rooms> call, @NonNull Throwable t) {

            }
        });

    }

    private void RequestAddRoom(String project_name, int project_id) {

        ProgressDialog pd = new ProgressDialog(this);
        pd.show();
        pd.setContentView(R.layout.progressbar);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        Call<AddData> call = req.addRoom(new AddData(true, project_name, project_id), shPref.getString(Link.token, null));
        call.enqueue(new Callback<AddData>() {
            @Override
            public void onResponse(@NonNull Call<AddData> call, @NonNull Response<AddData> response) {
                if (response.code() == 201) {
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                }

                if (response.code() == 400 || response.code() == 404) {
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, R.string.invalid_data, Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(@NonNull Call<AddData> call, @NonNull Throwable t) {
            }
        });
    }

    void showProjectDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.create_project_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        EditText edt_project = dialog.findViewById(R.id.project_name);
        AppCompatButton go = dialog.findViewById(R.id.go);
        AppCompatButton cancel = dialog.findViewById(R.id.cancel);


        go.setOnClickListener(v -> {

            String str_project = edt_project.getText().toString();


            if (!str_project.isEmpty() && str_project.length() > 3) {
                RequestAddProject(str_project);
                dialog.dismiss();
            }
            if (str_project.isEmpty()) {
                Toast.makeText(MainActivity.this, R.string.error_empty, Toast.LENGTH_SHORT).show();

            }


        });

        cancel.setOnClickListener(v -> {

            dialog.dismiss();

        });

        dialog.show();
    }

    void showRoomDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.create_room_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        EditText edt_project_name = dialog.findViewById(R.id.project_name);
        EditText edt_project_id = dialog.findViewById(R.id.project_id);
        AppCompatButton go = dialog.findViewById(R.id.go);
        AppCompatButton cancel = dialog.findViewById(R.id.cancel);


        go.setOnClickListener(v -> {

            String str_project_name = edt_project_name.getText().toString();
            String str_project_id = edt_project_id.getText().toString();


            if (!str_project_name.isEmpty() && str_project_name.length() > 3 &&
                    str_project_id.length() > 0) {
                RequestAddRoom(str_project_name, Integer.parseInt(str_project_id));
                dialog.dismiss();
            }
            if (str_project_name.isEmpty()) {
                Toast.makeText(MainActivity.this, R.string.error_empty, Toast.LENGTH_SHORT).show();

            }


        });

        cancel.setOnClickListener(v -> {

            dialog.dismiss();

        });

        dialog.show();
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

    private void RequestAddProject(String project_name) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.show();
        pd.setContentView(R.layout.progressbar);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        Call<AddData> call = req.addProject(new AddData(project_name, true), shPref.getString(Link.token, null));
        call.enqueue(new Callback<AddData>() {
            @Override
            public void onResponse(@NonNull Call<AddData> call, @NonNull Response<AddData> response) {
                if (response.code() == 201) {
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                }

                if (response.code() == 400 || response.code() == 404) {
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, R.string.invalid_data, Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(@NonNull Call<AddData> call, @NonNull Throwable t) {
            }
        });

    }

    private void RequestToGetProjects() {
        Call<Projects> call = req.getProjects(shPref.getString(Link.token, null));
        call.enqueue(new Callback<Projects>() {
            @Override
            public void onResponse(@NonNull Call<Projects> call, @NonNull Response<Projects> response) {
                if (response.code() == 200) {
                    Projects body = response.body();
                    projectsAdapter = new ProjectsAdapter(body, MainActivity.this);
                    rec_projects.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
                    rec_projects.setAdapter(projectsAdapter);
                    linear_projects.setVisibility(View.VISIBLE);
                    if (response.body().getObjects().isEmpty()) {
                        linear_projects.setVisibility(View.INVISIBLE);
                        linear_lottie.setVisibility(View.VISIBLE);
                    } else {
                        linear_lottie.setVisibility(View.INVISIBLE);
                        linear_projects.setVisibility(View.VISIBLE);
                        RequestToGetRooms();
                    }
                }

                if (response.code() == 400 || response.code() == 404) {
                    Toast.makeText(MainActivity.this, R.string.error_parsing, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(@NonNull Call<Projects> call, @NonNull Throwable t) {
            }
        });

    }


    private void RequestToGetRooms() {
        Call<Rooms> call = req.getRooms(shPref.getString(Link.token, null));
        call.enqueue(new Callback<Rooms>() {
            @Override
            public void onResponse(@NonNull Call<Rooms> call, @NonNull Response<Rooms> response) {
                if (response.code() == 200) {
                    Rooms body = response.body();
                    roomsAdapter = new RoomsAdapter(body, MainActivity.this);
                    rec_rooms.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
                    rec_rooms.setAdapter(roomsAdapter);
                    linear_rooms.setVisibility(View.VISIBLE);
                    if (response.body().getObjects().isEmpty()) {
                        linear_rooms.setVisibility(View.INVISIBLE);
                    } else {
                        RequestToGetDevices();
                    }
                }

                if (response.code() == 400 || response.code() == 404) {
                    Toast.makeText(MainActivity.this, R.string.error_parsing, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(@NonNull Call<Rooms> call, @NonNull Throwable t) {

            }
        });

    }


    private void RequestToGetDevices() {
        Call<Devices> call = req.getDevices(shPref.getString(Link.token, null));
        call.enqueue(new Callback<Devices>() {
            @Override
            public void onResponse(@NonNull Call<Devices> call, @NonNull Response<Devices> response) {
                if (response.code() == 200) {
                    Devices body = response.body();
                    devicesAdapter = new DevicesAdapter(body, MainActivity.this);
                    rec_devices.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
                    rec_devices.setAdapter(devicesAdapter);
                    linear_devices.setVisibility(View.VISIBLE);
                    if (response.body().getObjects().isEmpty()) {
                        linear_devices.setVisibility(View.INVISIBLE);
                    }
                }

                if (response.code() == 400 || response.code() == 404) {
                    Toast.makeText(MainActivity.this, R.string.error_parsing, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(@NonNull Call<Devices> call, @NonNull Throwable t) {
            }
        });

    }

    private void init() {

        refresh = findViewById(R.id.refresh);
        tv_firstPro = findViewById(R.id.tv_firstPro);
        floatBtn = findViewById(R.id.floatBtn);
        rec_projects = findViewById(R.id.rec_projects);
        rec_devices = findViewById(R.id.rec_devices);
        rec_rooms = findViewById(R.id.rec_rooms);
        linear_projects = findViewById(R.id.linear_projects);
        linear_rooms = findViewById(R.id.linear_rooms);
        linear_devices = findViewById(R.id.linear_devices);
        linear_lottie = findViewById(R.id.linear_lottie);
        req = ApiService.getApiClient();
        shPref = getSharedPreferences(Link.SharePref, Context.MODE_PRIVATE);
    }
}