package com.android.anmol.githubapi;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The list should populate as you type in
 * Use pagination
 * Handle orientation change
 * Demonstrate usage of popular libraries
 */
public class SearchActivity extends AppCompatActivity {

    private static final String KEY_LIST_SCROLL_POS = "KEY_LIST_SCROLL_POS";
    private static final int LOADER_ID = 1;

    private RecyclerView mRvUsers;
    private TextView mTvMsg;
    private ProgressBar mPbLoading;


    private List<UserModel> mUserList;
    private UsersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();

        if (savedInstanceState != null) {
            Parcelable listState = savedInstanceState.getParcelable(KEY_LIST_SCROLL_POS);
            mRvUsers.getLayoutManager().onRestoreInstanceState(listState);
        }
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
            Bundle args = new Bundle();
            args.putString("", "");
            args.putString("", "");
            args.putString("", "");

            mPbLoading.setVisibility(View.VISIBLE);
            Call<ResUserData> call = APIClient.getClient().create(ApiInterface.class).getUsers(param);
            call.enqueue(new ResponseListener());

//            getSupportLoaderManager().initLoader(LOADER_ID, args, new LoaderCallbackListener());

        }
    }

    private class LoaderCallbackListener implements LoaderManager.LoaderCallbacks<ResUserData> {
        @Override
        public Loader<ResUserData> onCreateLoader(int id, Bundle args) {
            return new NetworkCallLoader(SearchActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<ResUserData> loader, ResUserData users) {

            List<UserModel> newRes = new ResToUiUserList().convert(users.getData());

            Toast.makeText(SearchActivity.this, "aaga", Toast.LENGTH_LONG).show();

            if (mAdapter == null) {
                mAdapter = new UsersAdapter(SearchActivity.this, newRes);
                mRvUsers.setAdapter(mAdapter);
                mUserList = newRes;
            } else {
                mUserList.clear();
                if (newRes != null) {
                    mUserList.addAll(newRes);
                }
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onLoaderReset(Loader<ResUserData> loader) {
            mUserList.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ResponseListener implements Callback<ResUserData> {
        @Override
        public void onResponse(Call<ResUserData> call, Response<ResUserData> response) {

            mPbLoading.setVisibility(View.GONE);

            if (response == null) {
                onFailure(call, null);
                return;
            }
            ResUserData userRes = response.body();

            if (userRes == null || userRes.getData().isEmpty()) {
                onFailure(call, null);
                return;
            }

            mTvMsg.setVisibility(View.GONE);

            List<UserModel> newRes = new ResToUiUserList().convert(userRes.getData());

            if (mAdapter == null) {
                mAdapter = new UsersAdapter(SearchActivity.this, newRes);
                mRvUsers.setAdapter(mAdapter);
                mUserList = newRes;
            } else {
                mUserList.clear();
                if (newRes != null) {
                    mUserList.addAll(newRes);
                }
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(Call<ResUserData> call, Throwable t) {
            call.cancel();
            if (mUserList != null) {
                mUserList.clear();
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            mPbLoading.setVisibility(View.GONE);
            mTvMsg.setVisibility(View.VISIBLE);
        }
    }
}
