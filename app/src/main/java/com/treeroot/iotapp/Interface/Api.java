package com.treeroot.iotapp.Interface;


import com.treeroot.iotapp.Model.AddData;
import com.treeroot.iotapp.Model.Authentication;
import com.treeroot.iotapp.Model.DeviceType;
import com.treeroot.iotapp.Model.Devices;
import com.treeroot.iotapp.Model.Projects;
import com.treeroot.iotapp.Model.Rooms;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

    //Request From Add Device
    @POST("device/")
    Call<AddData> addDevice(
            @Body AddData authentication,@Header("Authorization") String auth);


    //Request From Add Room
    @POST("room/")
    Call<AddData> addRoom(
            @Body AddData authentication,@Header("Authorization") String auth);


    //Request From Add Project
    @POST("project/")
    Call<AddData> addProject(
            @Body AddData authentication,@Header("Authorization") String auth);


    //Request From Register
    @POST("auth/register/")
    Call<Authentication> register(
            @Body Authentication authentication);


    //Request From Login
    @POST("auth/login/")
    Call<Authentication> login(
            @Body Authentication authentication);


    //Request From getProjects
    @GET("project/")
    Call<Projects> getProjects(
            @Header("Authorization") String auth);


    //Request From getRooms
    @GET("room/")
    Call<Rooms> getRooms(
            @Header("Authorization") String auth);


    //Request From getDevices
    @GET("device/")
    Call<Devices> getDevices(
            @Header("Authorization") String auth);


    //Request From getDevicesType
    @GET("device_types/")
    Call<DeviceType> getDeviceType(
            @Header("Authorization") String auth);


    //Request From Update Project
    @PUT("project/{id}/")
    Call<AddData> updateProject(
            @Path("id") int id, @Body AddData authentication, @Header("Authorization") String auth);


    //Request From Update Room
    @PUT("room/{id}/")
    Call<AddData> updateRoom(
            @Path("id") int id, @Body AddData authentication, @Header("Authorization") String auth);


    //Request From Update Device
    @PUT("device/{id}/")
    Call<AddData> updateDevice(
            @Path("id") int id, @Body AddData authentication, @Header("Authorization") String auth);


    //Request From Delete Project
    @DELETE("project/{id}/")
    Call<JSONObject> deleteProject(
            @Path("id") int id, @Header("Authorization") String auth);


    //Request From Delete Room
    @DELETE("room/{id}/")
    Call<JSONObject> deleteRoom(
            @Path("id") int id, @Header("Authorization") String auth);


    //Request From Delete Device
    @DELETE("device/{id}/")
    Call<JSONObject> deleteDevice(
            @Path("id") int id, @Header("Authorization") String auth);

}












