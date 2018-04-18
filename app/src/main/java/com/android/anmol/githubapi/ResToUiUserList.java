package com.android.anmol.githubapi;

import android.support.annotation.Nullable;

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