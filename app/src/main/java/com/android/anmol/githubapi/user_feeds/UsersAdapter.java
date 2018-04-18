package com.android.anmol.githubapi.user_feeds;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.anmol.githubapi.R;
import com.android.anmol.githubapi.user_feeds.ui_handling.OnRvScrollListener;
import com.android.anmol.githubapi.user_feeds.viewholder.UserViewModel;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_LOADING = 1;
    private static final int VIEW_TYPE_DATA = 2;
    private Activity mActivity;
    private List<UserModel> mUsers;
    private boolean mShowLoader;

    public UsersAdapter(final RecyclerView recyclerView,
                        final Activity activity,
                        final List<UserModel> userList,
                        final OnRvScrollListener listener) {

        mActivity = activity;
        mUsers = userList;

        // Get the layout manager to detect the scroll.
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        // Set scroll listener on the recyclerView.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItems = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!mShowLoader && totalItems <= (lastVisibleItem + 1)) {
                    // Means the loader is not showing and the count of last element visible is same as the total
                    // number of items given to the recycler View to present, meaning that we can now load more
                    // elements in it.
                    if (listener != null) {
                        // Trigger the callback to laod more items in the recycler view.
                        listener.onScrollRecyclerView();
                    }

                    // Set the loading bar visible, to simulate loading of new items.
                    mShowLoader = true;
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_TYPE_DATA) {

            View inflatedView = LayoutInflater.from(mActivity).inflate(R.layout.item_row_layout, parent, false);
            viewHolder = new UserViewModel(inflatedView);
        } else if (viewType == VIEW_TYPE_LOADING) {
            // Case to show the loading bar.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_loading_item, parent, false);
            viewHolder = new LoadingViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof UserViewModel) {
            final UserModel userModel = mUsers.get(position);
            ((UserViewModel) holder).bind(userModel);
        } else if (holder instanceof LoadingViewHolder) {
            // Show loading bar.
            ((LoadingViewHolder) holder).setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers == null ? 0 : mUsers.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mUsers.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    public void stopLoading() {
        mShowLoader = false;
    }
}