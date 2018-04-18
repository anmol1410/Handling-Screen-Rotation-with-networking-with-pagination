package com.android.anmol.githubapi.data.source;

import android.support.annotation.NonNull;

public interface UserDataSource {

    interface FetchUsersCallback {

        void onUsersFetched(ResUserData users);

        void onDataNotAvailable();
    }

    void getUsers(@NonNull FetchUsersCallback callback, String queryParam);

    void cancelRequest();

}
