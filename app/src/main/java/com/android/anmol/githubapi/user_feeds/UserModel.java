package com.android.anmol.githubapi.user_feeds;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable{

    private String mUser;
    private String mUserImagePath;

    protected UserModel(Parcel in) {
        mUser = in.readString();
        mUserImagePath = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getUser() {
        return mUser;
    }

    public String getUserImagePath() {
        return mUserImagePath;
    }

    private UserModel(Builder builder) {
        mUser = builder.mUser;
        mUserImagePath = builder.mUserImagePath;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUser);
        dest.writeString(mUserImagePath);
    }


    public static final class Builder {
        private String mUser;
        private String mUserImagePath;

        private Builder() {
        }

        public Builder user(String val) {
            mUser = val;
            return this;
        }

        public Builder userImagePath(String val) {
            mUserImagePath = val;
            return this;
        }

        public UserModel build() {
            return new UserModel(this);
        }
    }
}
