package com.android.anmol.githubapi.data.source.remote;

import android.support.annotation.NonNull;

import com.android.anmol.githubapi.data.source.ResUserData;
import com.android.anmol.githubapi.data.source.UserDataSource;
import com.android.anmol.githubapi.service.APIClient;
import com.android.anmol.githubapi.service.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of the data source handling remote or network calls.
 */
public class UsersRemoteDataSource implements UserDataSource {

    /**
     * Singleton Instance.
     */
    private static UsersRemoteDataSource INSTANCE;

    /**
     * Network call instance.
     */
    private Call<ResUserData> mCall;

    /**
     * Get singleton instance.
     *
     * @return UsersRemoteDataSource instance.
     */
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
    public void getUsers(@NonNull final FetchUsersCallback callback, String queryParam, int page) {
        // Store the call, to cancel it in case required.
        mCall = APIClient.getClient().create(ApiInterface.class).getUsers(queryParam, page);

        // Make network request.
        mCall.enqueue(new Callback<ResUserData>() {
            @Override
            public void onResponse(Call<ResUserData> call, Response<ResUserData> response) {
                // Send back the response.
                callback.onUsersFetched(response.body());
            }

            @Override
            public void onFailure(Call<ResUserData> call, Throwable t) {
                if (call.isCanceled()) {
                    // as we may have called call.cancel() which will result in failure.
                    // So ignore these failure case, not required in our case.
                    return;
                }
                // Else send back the failure response.
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * Cancel the requests.
     */
    @Override
    public void cancelRequest() {
        if (mCall != null) {
            mCall.cancel();
            mCall = null;
        }
    }
}
