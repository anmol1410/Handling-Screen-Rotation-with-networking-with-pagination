package com.android.anmol.githubapi.utility.converter;

import android.support.annotation.Nullable;

import com.android.anmol.githubapi.data.source.ResUserModel;
import com.android.anmol.githubapi.user_feeds.UserModel;
import com.android.anmol.githubapi.base.ModelConverter;

import java.util.ArrayList;
import java.util.List;

public class ResToUiUserList implements ModelConverter<List<ResUserModel>, List<UserModel>> {

    @Nullable
    @Override
    public List<UserModel> convert(@Nullable List<ResUserModel> source) {
        if (source == null) {
            return null;
        }
        List<UserModel> list = new ArrayList<>();
        final ResuserToModelUserConverter modelConverter = new ResuserToModelUserConverter();
        for (ResUserModel resModel : source) {
            list.add(modelConverter.convert(resModel));
        }
        return list;
    }
}