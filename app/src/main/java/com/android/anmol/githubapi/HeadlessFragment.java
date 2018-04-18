package com.android.anmol.githubapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

import retrofit2.Call;
import retrofit2.Response;

public class HeadlessFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void sendRequest(String param) {
        mCall = APIClient.getClient().create(ApiInterface.class).getUsers(param);
        mCall.enqueue(new ResponseListener());
    }

    public void cancelRequest() {
        if (mCall != null) {
//            mCall.cancel();
        }
    }

    public interface OnFragmentInteractionListener {
        void onResponse(Response<ResUserData> uri);

        void onFailure();
    }

    private class ResponseListener implements retrofit2.Callback<ResUserData> {
        @Override
        public void onResponse(Call<ResUserData> call, Response<ResUserData> response) {
//            mListener.onResponse(response.body());

            sendData(response);
        }

        @Override
        public void onFailure(Call<ResUserData> call, Throwable t) {
//            mListener.onFailure();
            sendData(null);
        }
    }

    private void sendData(@Nullable Response<ResUserData> response) {
        Intent intent = new Intent("KEY");
        if (response != null) {
            intent.putExtra("Response", response.body());
        }
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }
}
