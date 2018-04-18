package com.android.anmol.githubapi;

import android.support.annotation.NonNull;

interface UserDataSource {

    interface FetchUsersCallback {

        void onUsersFetched(ResUserData users);

        void onDataNotAvailable();
    }

    void getUsers(@NonNull FetchUsersCallback callback, String queryParam);

}
