package com.android.anmol.githubapi;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

import retrofit2.Call;
import retrofit2.Response;

public class HeadlessFragment extends Fragment {

    private final String TAG = HeadlessFragment.class.getSimpleName();

    private static final String KEY_BROADCAST_INTENT_FILTER = "KEY_BROADCAST_INTENT_FILTER";
    private static final String KEY_RESPONSE = "KEY_RESPONSE";
    private Call<ResUserData> mCall;

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
        mCall = APIClient.getClient().create(ApiInterface.class).getUsers(param);
        mCall.enqueue(new ResponseListener());
    }

    public void cancelRequest() {
        MyLog.d(TAG, "Cancelling Request");
        if (mCall != null) {
//            mCall.cancel();
        }
    }

    private class ResponseListener implements retrofit2.Callback<ResUserData> {
        @Override
        public void onResponse(Call<ResUserData> call, Response<ResUserData> response) {
            MyLog.d(TAG, "Response received : " + response.body());
            sendData(response);
        }

        @Override
        public void onFailure(Call<ResUserData> call, Throwable t) {
            MyLog.e(TAG, "Response Failure : " + t);
            sendData(null);
        }
    }

    private void sendData(@Nullable Response<ResUserData> response) {
        Intent intent = new Intent(KEY_BROADCAST_INTENT_FILTER);
        if (response != null) {
            intent.putExtra(KEY_RESPONSE, response.body());
        }
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    public static ResUserData getResponseFromIntent(Intent intent) {
        return intent.getParcelableExtra(KEY_RESPONSE);
    }

    public static IntentFilter getIntentFilterForRegister() {
        return new IntentFilter(KEY_BROADCAST_INTENT_FILTER);
    }
}
