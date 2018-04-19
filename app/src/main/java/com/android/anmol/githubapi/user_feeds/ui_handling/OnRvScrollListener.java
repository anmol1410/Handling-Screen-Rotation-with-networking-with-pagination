package com.android.anmol.githubapi.user_feeds.ui_handling;

/**
 * Listener which is triggered when the recycler View is scrolled, to inform to load more items in it.
 */
public interface OnRvScrollListener {

    /**
     * Callback method, triggered to load more items in the recycler View.
     */
    void onScrollRecyclerView();
}
