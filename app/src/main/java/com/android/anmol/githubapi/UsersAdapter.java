package com.android.anmol.githubapi;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

class UsersAdapter extends RecyclerView.Adapter<UserViewModel> {
    private Activity mActivity;
    private List<UserModel> mUsers;

    UsersAdapter(Activity activity, List<UserModel> userList) {
        mActivity = activity;
        mUsers = userList;
    }

    @Override
    public UserViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(mActivity).inflate(R.layout.item_row_layout, parent, false);
        return new UserViewModel(inflatedView);
    }

    @Override
    public void onBindViewHolder(UserViewModel holder, int position) {
        final UserModel userModel = mUsers.get(position);
        holder.bind(userModel);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}