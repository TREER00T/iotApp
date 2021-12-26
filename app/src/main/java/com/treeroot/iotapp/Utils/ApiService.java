package com.treeroot.iotapp.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.treeroot.iotapp.Interface.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiService {

    public static Retrofit retrofit = null;
    public static Api api;

    public static Api getApiClient() {

        String url = "https://iot.abrbit.com/api/v1/";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            api =retrofit.create(Api.class);
        }
        return api;

    }

}
