package com.android.anmol.githubapi;

public class UserModel {

    private String mUser;
    private String mUserImagePath;

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
