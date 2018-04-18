package com.android.anmol.githubapi.data.source;

import android.support.annotation.NonNull;

/**
 * Main entry point for accessing Users data.
 * <p>
 * For simplicity, only getUsers() has callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 */
public interface UserDataSource {

    interface FetchUsersCallback {

        void onUsersFetched(ResUserData users);

        void onDataNotAvailable();
    }

    void getUsers(@NonNull FetchUsersCallback callback, String queryParam, int page);

    void cancelRequest();
}
