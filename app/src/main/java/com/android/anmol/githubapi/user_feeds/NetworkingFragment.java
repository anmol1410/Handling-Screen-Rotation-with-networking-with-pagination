package com.android.anmol.githubapi.user_feeds;

        import android.content.Intent;
        import android.content.IntentFilter;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v4.content.LocalBroadcastManager;

        import com.android.anmol.githubapi.user_feeds.presenter.UsersContract;
        import com.android.anmol.githubapi.utility.logging.MyLog;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Headless fragment to make network calls, so to be aware with activity rotations.
 */
public class NetworkingFragment extends Fragment implements UsersContract.View {

    // TAG for logging purposes.
    private final String TAG = NetworkingFragment.class.getSimpleName();

    // KEYS to send the local broadcast back to the calling activity.
    private static final String KEY_PAGE_COUNT = "KEY_PAGE_COUNT";
    private static final String KEY_BROADCAST_INTENT_FILTER = "KEY_BROADCAST_INTENT_FILTER";
    private static final String KEY_RESPONSE = "KEY_RESPONSE";

    // Presenter to handle the business logics.
    private UsersContract.FeedDetailsPresenter mPresenter;

    // Page count to identify which page we are working on currently.
    private int mPageCount;

    public NetworkingFragment() {
        // Required empty public constructor
    }

    public static NetworkingFragment newInstance() {
        return new NetworkingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    /**
     * Send the request to get the uses.
     *
     * @param param Query to search the users from.
     * @param page  Page count of that user result.
     */
    public void sendRequest(String param, int page) {
        mPageCount = page;

        MyLog.d(TAG, "Sending Request");
        mPresenter.getUsers(param, page);
    }

    @Override
    public void setPresenter(UsersContract.FeedDetailsPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onUsersFetched(List<UserModel> users) {
        sendData(users);
    }

    @Override
    public void onUsersFetchFailure() {
        sendData(null);
    }

    @Override
    public void onPreLoad() {
        ((SearchActivity) getContext()).onPreLoad();
    }

    /**
     * Send the result to the activity, to show it on UI.
     *
     * @param response Network response of the list of users.
     */
    private void sendData(@Nullable List<UserModel> response) {
        Intent intent = new Intent(KEY_BROADCAST_INTENT_FILTER);
        intent.putExtra(KEY_PAGE_COUNT, mPageCount);
        if (response != null) {
            intent.putParcelableArrayListExtra(KEY_RESPONSE, (ArrayList<UserModel>) response);
        }
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    /**
     * Reads the Response from the intent.
     *
     * @param intent Intent to read the response from.
     * @return Response read from intent.
     */
    public static List<UserModel> getResponseFromIntent(Intent intent) {
        return intent.getParcelableArrayListExtra(KEY_RESPONSE);
    }

    /**
     * Reads the Page count from the intent.
     *
     * @param intent Intent to read the Page count from.
     * @return Page count read from intent.
     */
    public static int getPageCountFromIntent(Intent intent) {
        return intent.getIntExtra(KEY_PAGE_COUNT, 0);
    }

    /**
     * Provides IntentFilter to register tje broadcast receiver for communication from fragment -> activity
     *
     * @return IntentFilter.
     */
    public static IntentFilter getIntentFilterForRegister() {
        return new IntentFilter(KEY_BROADCAST_INTENT_FILTER);
    }
}
