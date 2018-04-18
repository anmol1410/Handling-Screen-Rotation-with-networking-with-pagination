package com.android.anmol.githubapi;

import com.google.gson.annotations.SerializedName;

class ResUserModel {

    @SerializedName("login")
    private String mUser;

    @SerializedName("avatar_url")
    private String mUserImagePath;

    public String getUser() {
        return mUser;
    }

    public void setUser(String mUser) {
        this.mUser = mUser;
    }

    public String getUserImagePath() {
        return mUserImagePath;
    }

    public void setUserImagePath(String mUserImagePath) {
        this.mUserImagePath = mUserImagePath;
    }
}
