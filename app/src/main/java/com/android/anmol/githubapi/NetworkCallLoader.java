package com.android.anmol.githubapi;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class NetworkCallLoader<T> extends AsyncTaskLoader<T> {

    private T mResponse;

    public NetworkCallLoader(Context ctx) {
        // Loaders may be used across multiple Activitys (assuming they aren't
        // bound to the LoaderManager), so NEVER hold a reference to the context
        // directly. Doing so will cause you to leak an entire Activity's context.
        // The superclass constructor will store a reference to the Application
        // Context instead, and can be retrieved with a call to getContext().
        super(ctx);
    }

    /**
     * This method is called on a background thread and generates a List of
     * {@link ResUserData} objects.
     */
    @Override
    public T loadInBackground() {
        // Network call
        // api.github.com/search/users?q=text

        ReqUserData user = new ReqUserData();
        Call<ResUserData> call = APIClient.getClient().create(ApiInterface.class).getUsers(user.getQueryText());
        call.enqueue(new Callback<ResUserData>() {
            @Override
            public void onResponse(Call<ResUserData> call, Response<ResUserData> response) {
                ResUserData userRes = response.body();
            }

            @Override
            public void onFailure(Call<ResUserData> call, Throwable t) {
                call.cancel();
            }
        });
        return null;
    }

    /*******************************************/
    /** (2) Deliver the results to the client **/
    /*******************************************/

    /**
     * Called when there is new data to deliver to the client. The superclass will
     * deliver it to the registered listener (i.e. the LoaderManager), which will
     * forward the results to the client through a call to onLoadFinished.
     */
    @Override
    public void deliverResult(T response) {
        if (isReset()) {

            // The Loader has been reset; ignore the result and invalidate the data.
            // This can happen when the Loader is reset while an asynchronous query
            // is working in the background. That is, when the background thread
            // finishes its work and attempts to deliver the results to the client,
            // it will see here that the Loader has been reset and discard any
            // resources associated with the new data as necessary.
            if (response != null) {
                return;
            }
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        T oldResponse = mResponse;
        mResponse = response;

        if (isStarted()) {
            // If the Loader is in a started state, have the superclass deliver the
            // results to the client.
            super.deliverResult(response);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldResponse != null && oldResponse != response) {
//            releaseResources(oldResponse);
        }
    }

    /*********************************************************/
    /** (3) Implement the Loader�s state-dependent behavior **/
    /*********************************************************/

    @Override
    protected void onStartLoading() {
        if (mResponse != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mResponse);
        }

        if (takeContentChanged()) {
            // When the observer detects a new installed application, it will call
            // onContentChanged() on the Loader, which will cause the next call to
            // takeContentChanged() to return true. If this is ever the case (or if
            // the current data is null), we force a new load.
            forceLoad();
        } else if (mResponse == null) {
            // If the current data is null... then we should make it non-null! :)
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {

        // The Loader has been put in a stopped state, so we should attempt to
        // cancel the current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is; Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {

        // Ensure the loader is stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'apps'.
        if (mResponse != null) {
            mResponse = null;
        }
    }

    @Override
    public void onCanceled(T users) {

        // Attempt to cancel the current asynchronous load.
        super.onCanceled(users);

    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }
}