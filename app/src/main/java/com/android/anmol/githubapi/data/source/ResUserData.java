package com.android.anmol.githubapi.data.source;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResUserData implements Parcelable{

    @SerializedName("items")
    private List<ResUserModel> mData;

    protected ResUserData(Parcel in) {
    }

    public static final Creator<ResUserData> CREATOR = new Creator<ResUserData>() {
        @Override
        public ResUserData createFromParcel(Parcel in) {
            return new ResUserData(in);
        }

        @Override
        public ResUserData[] newArray(int size) {
            return new ResUserData[size];
        }
    };

    public List<ResUserModel> getData() {
        return mData;
    }

    public void setData(List<ResUserModel> data) {
        this.mData = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
