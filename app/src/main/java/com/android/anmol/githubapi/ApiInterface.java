package com.android.anmol.githubapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/search/users")
    Call<ResUserData> getUsers(@Query("q") String queryText);

}