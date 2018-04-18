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

    private static final String KEY_LIST_SCROLL_POS = "KEY_LIST_SCROLL_POS";
    private static final String KEY_USER_LIST = "KEY_USER_LIST";
    private static final String KEY_UNDER_PROGRESS = "KEY_UNDER_PROGRESS";
    private static final String TAG = SearchActivity.class.getSimpleName();

    private RecyclerView mRvUsers;
    private TextView mTvMsg;
    private ProgressBar mPbLoading;

    private List<UserModel> mUserList = new ArrayList<>();
    private UsersAdapter mAdapter;
    private HeadlessFragment mHeadlessFragment;
    private final String FRAGMENT_TAG = "HeadlessFragment";
    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            List<UserModel> userRes = HeadlessFragment.getResponseFromIntent(intent);

            if (userRes != null) {
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
                mHeadlessFragment.cancelRequest();
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

        mHeadlessFragment = (HeadlessFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (mHeadlessFragment == null) {
            mHeadlessFragment = HeadlessFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(mHeadlessFragment, FRAGMENT_TAG).commit();
        }

        if (savedInstanceState != null) {
            mTvMsg.setVisibility(View.GONE);

            if (savedInstanceState.getBoolean(KEY_UNDER_PROGRESS)) {
                mPbLoading.setVisibility(View.VISIBLE);
            }

            mUserList = savedInstanceState.getParcelableArrayList(KEY_USER_LIST);
            setAdapter(mUserList);

            Parcelable listState = savedInstanceState.getParcelable(KEY_LIST_SCROLL_POS);
            mRvUsers.getLayoutManager().onRestoreInstanceState(listState);
        }

        new UsersPresenter(
                Injection.provideUsersRepository(),
                mHeadlessFragment);
    }

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
        Parcelable listState = mRvUsers.getLayoutManager().onSaveInstanceState();
        savedInstanceState.putParcelable(KEY_LIST_SCROLL_POS, listState);
        savedInstanceState.putParcelableArrayList(KEY_USER_LIST, (ArrayList) mUserList);
        savedInstanceState.putBoolean(KEY_UNDER_PROGRESS, mPbLoading.getVisibility() == View.VISIBLE);
    }

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
            MyLog.i(TAG, "Query param : " + param);

            mPbLoading.setVisibility(View.VISIBLE);
            mTvMsg.setVisibility(View.GONE);

            mHeadlessFragment.cancelRequest();
            mHeadlessFragment.sendRequest(param);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyLog.i(TAG, "Broadcast registered");
        LocalBroadcastManager.getInstance(this).registerReceiver(mResultReceiver, HeadlessFragment.getIntentFilterForRegister());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyLog.i(TAG, "Broadcast unregistered");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mResultReceiver);
    }

    private void setAdapter(List<UserModel> newRes) {
        mAdapter = new UsersAdapter(SearchActivity.this, newRes);
        mRvUsers.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHeadlessFragment != null) {
            mHeadlessFragment = null;
        }
    }
}