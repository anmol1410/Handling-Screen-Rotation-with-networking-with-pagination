package com.android.anmol.githubapi.user_feeds;

import android.os.Parcel;
import android.os.Parcelable;

class ReqUserData implements Parcelable {

    private String mQueryText;

    public ReqUserData() {
    }

    protected ReqUserData(Parcel in) {
        mQueryText = in.readString();
    }

    public static final Creator<ReqUserData> CREATOR = new Creator<ReqUserData>() {
        @Override
        public ReqUserData createFromParcel(Parcel in) {
            return new ReqUserData(in);
        }

        @Override
        public ReqUserData[] newArray(int size) {
            return new ReqUserData[size];
        }
    };

    public String getQueryText() {
        return mQueryText;
    }

    public void setQueryText(String mQueryText) {
        this.mQueryText = mQueryText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mQueryText);
    }
}
