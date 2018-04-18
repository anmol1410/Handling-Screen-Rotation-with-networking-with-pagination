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

    private final String TAG = HeadlessFragment.class.getSimpleName();

    private static final String KEY_BROADCAST_INTENT_FILTER = "KEY_BROADCAST_INTENT_FILTER";
    private static final String KEY_RESPONSE = "KEY_RESPONSE";
    private Call<ResUserData> mCall;
    private UsersContract.FeedDetailsPresenter mPresenter;

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

    @Override
    public void onDetach() {
        super.onDetach();
        cancelRequest();
    }

    public void sendRequest(String param) {
        MyLog.d(TAG, "Sending Request");
        mPresenter.getUsers(param);
//        mCall = APIClient.getClient().create(ApiInterface.class).getUsers(param);
//        mCall.enqueue(new ResponseListener());
    }

    public void cancelRequest() {
        MyLog.d(TAG, "Cancelling Request");
        if (mCall != null) {
//            mCall.cancel();
        }
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

    private void sendData(@Nullable List<UserModel> response) {
        Intent intent = new Intent(KEY_BROADCAST_INTENT_FILTER);
        if (response != null) {
            intent.putParcelableArrayListExtra(KEY_RESPONSE, (ArrayList<UserModel>) response);
        }
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    public static List<UserModel> getResponseFromIntent(Intent intent) {
        return intent.getParcelableArrayListExtra(KEY_RESPONSE);
    }

    public static IntentFilter getIntentFilterForRegister() {
        return new IntentFilter(KEY_BROADCAST_INTENT_FILTER);
    }
}
