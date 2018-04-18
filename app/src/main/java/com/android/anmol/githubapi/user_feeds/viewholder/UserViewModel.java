package com.android.anmol.githubapi.user_feeds.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.anmol.githubapi.R;
import com.android.anmol.githubapi.user_feeds.UserModel;
import com.android.anmol.githubapi.utility.ImageLoadUtils;

public class UserViewModel extends RecyclerView.ViewHolder {

    private final TextView mTvUser;
    private final ImageView mIvUserImage;
    private final ProgressBar mPbImageLoading;

    public UserViewModel(View itemView) {
        super(itemView);
        mTvUser = (TextView) itemView.findViewById(R.id.tv_user);
        mIvUserImage = (ImageView) itemView.findViewById(R.id.iv_user_image);
        mPbImageLoading = (ProgressBar) itemView.findViewById(R.id.pb_image_load);
    }

    public void bind(UserModel userModel) {
        mTvUser.setText(userModel.getUser());

        ImageLoadUtils.loadImage(userModel.getUserImagePath(), mIvUserImage, mPbImageLoading);
    }
}
