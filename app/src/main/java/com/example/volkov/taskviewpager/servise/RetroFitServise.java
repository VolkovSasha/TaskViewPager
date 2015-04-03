package com.example.volkov.taskviewpager.servise;

import com.example.volkov.taskviewpager.global.Constants;
import com.example.volkov.taskviewpager.model.ListModel;
import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

public interface RetroFitServise {

    @Headers("Authorization: " + Constants.APP_TOKEN)
    @GET("/api/{key}")
    void getList(@Path("key") String ordinalKey,
                 Callback<List<ListModel>> _model);
}
