package com.android.anmol.githubapi.service;

import com.android.anmol.githubapi.data.source.ResUserData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/search/users")
    Call<ResUserData> getUsers(@Query("q") String queryText);

}