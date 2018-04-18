package com.android.anmol.githubapi.data.source;

import android.support.annotation.NonNull;
import android.util.LruCache;

import com.android.anmol.githubapi.utility.Utils;

public class UsersRepository implements UserDataSource {

    private static UsersRepository INSTANCE = null;

    private final UserDataSource mUsersRemoteDataSource;

    private LruCache<String, ResUserData> mCachedResponse = new LruCache<>(200);

    // Prevent direct instantiation.
    private UsersRepository(@NonNull UserDataSource userRemoteDataSource) {
        mUsersRemoteDataSource = Utils.checkNotNull(userRemoteDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param feedsRemoteDataSource the backend data source
     * @return the {@link UsersRepository} instance
     */
    public static UsersRepository getInstance(UserDataSource feedsRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new UsersRepository(feedsRemoteDataSource);
        }
        return INSTANCE;
    }

    /**
     * Gets Feeds from cache, local data source or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link FetchUsersCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getUsers(@NonNull FetchUsersCallback callback, String queryParam, int page) {
        Utils.checkNotNull(callback);

        if (mCachedResponse != null && mCachedResponse.get(queryParam) != null) {
            callback.onUsersFetched(mCachedResponse.get(queryParam));
        } else {
            getFeedsFromRemoteDataSource(callback, queryParam, page);
        }
    }

    @Override
    public void cancelRequest() {
        mUsersRemoteDataSource.cancelRequest();
    }

    private void getFeedsFromRemoteDataSource(@NonNull final FetchUsersCallback callback,
                                              final String queryParam,
                                              final int page) {
        mUsersRemoteDataSource.getUsers(new FetchUsersCallback() {

            @Override
            public void onUsersFetched(ResUserData users) {
                mCachedResponse.put(queryParam, users);
                callback.onUsersFetched(users);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        }, queryParam, page);
    }
}