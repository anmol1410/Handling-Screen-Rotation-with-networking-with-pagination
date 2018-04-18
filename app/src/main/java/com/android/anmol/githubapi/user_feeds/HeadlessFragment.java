package com.android.anmol.githubapi.user_feeds;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

import com.android.anmol.githubapi.data.source.ResUserData;
import com.android.anmol.githubapi.user_feeds.presenter.UsersContract;
import com.android.anmol.githubapi.utility.logging.MyLog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class HeadlessFragment extends Fragment implements UsersContract.View {

    private static final String KEY_PAGE_COUNT = "KEY_PAGE_COUNT";
    private final String TAG = HeadlessFragment.class.getSimpleName();

    private static final String KEY_BROADCAST_INTENT_FILTER = "KEY_BROADCAST_INTENT_FILTER";
    private static final String KEY_RESPONSE = "KEY_RESPONSE";
    private Call<ResUserData> mCall;
    private UsersContract.FeedDetailsPresenter mPresenter;
    private int mPageCount;

    public HeadlessFragment() {
        // Required empty public constructor
    }

    public static HeadlessFragment newInstance() {
        return new HeadlessFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

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
        ((SearchActivity)getContext()).onPreLoad();
    }

    private void sendData(@Nullable List<UserModel> response) {
        Intent intent = new Intent(KEY_BROADCAST_INTENT_FILTER);
        intent.putExtra(KEY_PAGE_COUNT, mPageCount);
        if (response != null) {
            intent.putParcelableArrayListExtra(KEY_RESPONSE, (ArrayList<UserModel>) response);
        }
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    public static List<UserModel> getResponseFromIntent(Intent intent) {
        return intent.getParcelableArrayListExtra(KEY_RESPONSE);
    }

    public static int getPageCountFromIntent(Intent intent) {
        return intent.getIntExtra(KEY_PAGE_COUNT, 0);
    }

    public static IntentFilter getIntentFilterForRegister() {
        return new IntentFilter(KEY_BROADCAST_INTENT_FILTER);
    }
}
