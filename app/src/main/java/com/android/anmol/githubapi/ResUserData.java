package com.android.anmol.githubapi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ResUserData {

    @SerializedName("items")
    private List<ResUserModel> mData;

    public List<ResUserModel> getData() {
        return mData;
    }

    public void setData(List<ResUserModel> data) {
        this.mData = data;
    }
}
