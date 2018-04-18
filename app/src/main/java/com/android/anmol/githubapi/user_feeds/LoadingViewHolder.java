package com.android.anmol.githubapi.user_feeds;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.android.anmol.githubapi.R;

class LoadingViewHolder extends RecyclerView.ViewHolder {

    /**
     * Progress Bar view item.
     */
    private ProgressBar mPbEmpLoading;

    public LoadingViewHolder(View itemView) {
        super(itemView);

        // init the views.
        mPbEmpLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading_item);
    }

    /**
     * To set properties for the progress bar.
     * Here the progress bar is being set Indeterminate or not.
     *
     * @param isIndeterminate true to set {@link ProgressBar} Indeterminate,  false otherwise.
     */
    public void setIndeterminate(boolean isIndeterminate) {
        mPbEmpLoading.setIndeterminate(isIndeterminate);
    }

}
