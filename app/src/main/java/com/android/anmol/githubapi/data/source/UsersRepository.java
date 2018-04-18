package com.android.anmol.githubapi.data.source;

import android.support.annotation.NonNull;
import android.util.LruCache;

import com.android.anmol.githubapi.utility.Utils;

/**
 * Implementation to load Feeds from Data source.
 */
public class UsersRepository implements UserDataSource {

    private static UsersRepository INSTANCE = null;

    private final UserDataSource mUsersRemoteDataSource;

    private LruCache<CacheModel, ResUserData> mCachedResponse = new LruCache<>(200);

    // Prevent direct instantiation.
    private UsersRepository(@NonNull UserDataSource userRemoteDataSource) {
        mUsersRemoteDataSource = Utils.checkNotNull(userRemoteDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param userRemoteDataSource the backend data source
     * @return the {@link UsersRepository} instance
     */
    public static UsersRepository getInstance(UserDataSource userRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new UsersRepository(userRemoteDataSource);
        }
        return INSTANCE;
    }

    /**
     * Gets User data from cache, local data source or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link FetchUsersCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getUsers(@NonNull FetchUsersCallback callback, String queryParam, int page) {
        Utils.checkNotNull(callback);

        CacheModel cacheModel = new CacheModel(queryParam, page);
        if (mCachedResponse != null && mCachedResponse.get(cacheModel) != null) {
            // Present in cache, send result back now.
            callback.onUsersFetched(mCachedResponse.get(cacheModel));
        } else {
            // Not present in cache, hence hit the network call.
            getFeedsFromRemoteDataSource(callback, queryParam, page);
        }
    }

    @Override
    public void cancelRequest() {
        mUsersRemoteDataSource.cancelRequest();
    }

    /**
     * Get User from the Network i.e. remote data source.
     *
     * @param callback   {@Link FetchUsersCallback} to send the result back to its implementation.
     * @param queryParam Query to search the users from.
     * @param page       Page count so that load the result corresponding to this page.
     */
    private void getFeedsFromRemoteDataSource(@NonNull final FetchUsersCallback callback,
                                              final String queryParam,
                                              final int page) {
        mUsersRemoteDataSource.getUsers(new FetchUsersCallback() {

            @Override
            public void onUsersFetched(ResUserData users) {
                // Save result in cache and send back.
                mCachedResponse.put(new CacheModel(queryParam, page), users);
                callback.onUsersFetched(users);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        }, queryParam, page);
    }
}