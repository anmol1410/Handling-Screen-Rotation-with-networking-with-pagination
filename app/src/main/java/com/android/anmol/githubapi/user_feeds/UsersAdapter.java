package com.android.anmol.githubapi.user_feeds;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.anmol.githubapi.R;
import com.android.anmol.githubapi.user_feeds.viewholder.UserViewModel;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UserViewModel> {
    private Activity mActivity;
    private List<UserModel> mUsers;
    private boolean mIsLoadingAdded = false;
    private static final int LOADING = 1;
    private static final int ITEM = 2;

    public UsersAdapter(Activity activity, List<UserModel> userList) {
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
        return mUsers == null ? 0 : mUsers.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        return (position == mUsers.size() - 1 && mIsLoadingAdded) ? LOADING : ITEM;
//    }
}