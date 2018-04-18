package com.android.anmol.githubapi.data.source.remote;

import android.support.annotation.NonNull;

import com.android.anmol.githubapi.service.APIClient;
import com.android.anmol.githubapi.service.ApiInterface;
import com.android.anmol.githubapi.data.source.ResUserData;
import com.android.anmol.githubapi.data.source.UserDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersRemoteDataSource implements UserDataSource {

    private static UsersRemoteDataSource INSTANCE;

    public static UsersRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private UsersRemoteDataSource() {

    }

    @Override
    public void getUsers(@NonNull final FetchUsersCallback callback, String queryParam) {
        Call<ResUserData> mCall = APIClient.getClient().create(ApiInterface.class).getUsers(queryParam);
        mCall.enqueue(new Callback<ResUserData>() {
            @Override
            public void onResponse(Call<ResUserData> call, Response<ResUserData> response) {
                callback.onUsersFetched(response.body());
            }

            @Override
            public void onFailure(Call<ResUserData> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }
}
