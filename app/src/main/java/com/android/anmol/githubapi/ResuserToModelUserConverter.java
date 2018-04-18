package com.android.anmol.githubapi;

import android.support.annotation.Nullable;

public class ResuserToModelUserConverter implements ModelConverter<ResUserModel, UserModel> {

    @Nullable
    @Override
    public UserModel convert(@Nullable ResUserModel source) {

        if (source == null) {
            return null;
        }

        return UserModel.newBuilder()
                .user(source.getUser())
                .userImagePath(source.getUserImagePath())
                .build();
    }
}