package com.android.anmol.githubapi.user_feeds;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.anmol.githubapi.R;
import com.android.anmol.githubapi.base.Injection;
import com.android.anmol.githubapi.user_feeds.presenter.UsersPresenter;
import com.android.anmol.githubapi.user_feeds.ui_handling.OnRvScrollListener;
import com.android.anmol.githubapi.user_feeds.ui_handling.RecyclerViewSpaceItemDecorator;
import com.android.anmol.githubapi.utility.logging.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * The list should populate as you type in
 * Use pagination
 * Handle orientation change
 * Demonstrate usage of popular libraries
 */
public class SearchActivity extends AppCompatActivity {

    // KEYS for screen rotation save-state purposes.
    private static final String KEY_LIST_SCROLL_POS = "KEY_LIST_SCROLL_POS";
    private static final String KEY_USER_LIST = "KEY_USER_LIST";
    private static final String KEY_UNDER_PROGRESS = "KEY_UNDER_PROGRESS";
    private static final String KEY_QUERY_PARAM = "KEY_QUERY_PARAM";
    private static final String KEY_QUERY_PAGE_COUNT = "KEY_QUERY_PAGE_COUNT";

    // For logging purpose.
    private static final String TAG = SearchActivity.class.getSimpleName();

    /**
     * Recycler view to show the list of users.
     */
    private RecyclerView mRvUsers;

    /**
     * Message to show error etc. to the user.
     */
    private TextView mTvMsg;

    /**
     * Progress bar to indicate something is being done.
     */
    private ProgressBar mPbLoading;

    /**
     * List of users to show on UI.
     */
    private List<UserModel> mUserList = new ArrayList<>();

    /**
     * Adapter to load the items.
     */
    private UsersAdapter mAdapter;
    private NetworkingFragment mNetworkingFragment;
    private final String FRAGMENT_TAG = "NetworkingFragment";

    /**
     * Start loading starting from the page count = 0.
     */
    private int mPageCount = 0;

    private String mParam;
    /**
     * Receives the user response.
     */
    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            List<UserModel> userRes = NetworkingFragment.getResponseFromIntent(intent);

            if (NetworkingFragment.getPageCountFromIntent(intent) != 0) {
                // Means loading next page for some query.
                // So change the UI accordingly.

                // remove the last entry, which was the progress bar(null entry).
                mUserList.remove(mUserList.size() - 1);

                // Notify the adapter of this changed item.
                mAdapter.notifyItemRemoved(mUserList.size());

                if (userRes != null) {
                    // Add new data in the users list.
                    mUserList.addAll(userRes);

                    // Notify the Adapter that the items has changed.
                    mAdapter.notifyDataSetChanged();

                }
                //Stop the loading bar as the data has been loaded.
                mAdapter.stopLoading();
                return;
            }

            if (userRes != null) {
                // Means got some response to show.

                mPbLoading.setVisibility(View.GONE);
                mTvMsg.setVisibility(View.GONE);

                if (mAdapter == null) {
                    setAdapter(userRes);
                    mUserList = userRes;
                } else {
                    mUserList.clear();
                    mUserList.addAll(userRes);
                }
                mAdapter.notifyDataSetChanged();
            } else {
                // No data to show, hence clearing the previously shown data if any.
                if (mUserList != null) {
                    mUserList.clear();
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                }

                mPbLoading.setVisibility(View.GONE);
                mTvMsg.setVisibility(View.VISIBLE);
                mTvMsg.setText(R.string.err_fetching_data);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();

        // Setup Headless fragment which will be aware of activity lifecycle changes.
        mNetworkingFragment = (NetworkingFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (mNetworkingFragment == null) {
            mNetworkingFragment = NetworkingFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(mNetworkingFragment, FRAGMENT_TAG).commit();
        }

        if (savedInstanceState != null) {
            // Save last state of recycler view.

            mTvMsg.setVisibility(View.GONE);

            if (savedInstanceState.getBoolean(KEY_UNDER_PROGRESS)) {
                // Means network call is still under progress.
                mPbLoading.setVisibility(View.VISIBLE);
            }

            // Populate user list again.
            mUserList = savedInstanceState.getParcelableArrayList(KEY_USER_LIST);
            setAdapter(mUserList);

            // Scroll to the last state before rotation.
            Parcelable listState = savedInstanceState.getParcelable(KEY_LIST_SCROLL_POS);
            mRvUsers.getLayoutManager().onRestoreInstanceState(listState);

            // Save last  params for further requests.
            mParam = savedInstanceState.getString(KEY_QUERY_PARAM);
            mPageCount = savedInstanceState.getInt(KEY_QUERY_PAGE_COUNT, 0);
        }

        // Instantiate the presenter.
        new UsersPresenter(
                Injection.provideUsersRepository(),
                mNetworkingFragment);
    }

    /**
     * Instantiate views.
     */
    private void initViews() {
        mRvUsers = (RecyclerView) findViewById(R.id.rv_users);
        mTvMsg = (TextView) findViewById(R.id.tv_msg);
        mPbLoading = (ProgressBar) findViewById(R.id.pb_load);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        mRvUsers.setHasFixedSize(true);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvUsers.setLayoutManager(layoutManager);
        mRvUsers.setItemAnimator(new DefaultItemAnimator());
        mRvUsers.addItemDecoration(new RecyclerViewSpaceItemDecorator(this, R.dimen.rv_divider));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_action_item_search).getActionView();
        searchView.setOnQueryTextListener(new SearchQueryListener());
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save populated user data, and scroll position.
        Parcelable listState = mRvUsers.getLayoutManager().onSaveInstanceState();
        savedInstanceState.putParcelable(KEY_LIST_SCROLL_POS, listState);
        savedInstanceState.putParcelableArrayList(KEY_USER_LIST, (ArrayList) mUserList);
        savedInstanceState.putBoolean(KEY_UNDER_PROGRESS, mPbLoading.getVisibility() == View.VISIBLE);
        savedInstanceState.putString(KEY_QUERY_PARAM, mParam);
        savedInstanceState.putInt(KEY_QUERY_PAGE_COUNT, mPageCount);
    }

    /**
     * Handle UI change for before any network call is made.
     */
    public void onPreLoad() {
        mPbLoading.setVisibility(View.VISIBLE);
        mTvMsg.setVisibility(View.GONE);
    }

    /**
     * Listens to the search query changes.
     */
    private class SearchQueryListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            sendRequest(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            sendRequest(newText);
            return false;
        }

        void sendRequest(String param) {
            mParam = param;
            MyLog.i(TAG, "Query param : " + param);
            mNetworkingFragment.sendRequest(param, mPageCount = 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyLog.i(TAG, "Broadcast registered");
        LocalBroadcastManager.getInstance(this).registerReceiver(mResultReceiver, NetworkingFragment.getIntentFilterForRegister());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyLog.i(TAG, "Broadcast unregistered");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mResultReceiver);
    }

    private void setAdapter(List<UserModel> newRes) {
        mAdapter = new UsersAdapter(mRvUsers, SearchActivity.this, newRes, new ScrollListener());
        mRvUsers.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNetworkingFragment != null) {
            // Remove the fragment associated with it.
            mNetworkingFragment = null;
        }
    }

    /**
     * Handles scrolling of recycler view.
     * Triggered when the user has scrolled to the bottom of the screen.
     */
    private class ScrollListener implements OnRvScrollListener {
        @Override
        public void onScrollRecyclerView() {
            // Means the recycler view was scrolled to the bottom.

            // Add null at the last, so that progress bar will be shown for null item.
            mUserList.add(null);

            // Notify the Adapter of this new item set.
            mAdapter.notifyItemInserted(mUserList.size() - 1);

            mNetworkingFragment.sendRequest(mParam, ++mPageCount);
        }
    }
}