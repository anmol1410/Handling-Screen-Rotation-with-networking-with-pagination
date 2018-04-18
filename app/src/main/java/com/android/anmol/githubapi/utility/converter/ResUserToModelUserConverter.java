package com.android.anmol.githubapi.utility.converter;

import android.support.annotation.Nullable;

import com.android.anmol.githubapi.base.ModelConverter;
import com.android.anmol.githubapi.data.source.ResUserModel;
import com.android.anmol.githubapi.user_feeds.UserModel;

/**
 * Converts eac'h response item model to UI data model.
 */
public class ResUserToModelUserConverter implements ModelConverter<ResUserModel, UserModel> {

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