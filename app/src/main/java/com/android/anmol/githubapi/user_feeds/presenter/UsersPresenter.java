package com.android.anmol.githubapi.user_feeds.presenter;

import com.android.anmol.githubapi.data.source.ResUserData;
import com.android.anmol.githubapi.data.source.UserDataSource;
import com.android.anmol.githubapi.data.source.UsersRepository;
import com.android.anmol.githubapi.user_feeds.UserModel;
import com.android.anmol.githubapi.utility.Utils;
import com.android.anmol.githubapi.utility.converter.ResToUiUserList;

import java.util.List;

public class UsersPresenter implements UsersContract.FeedDetailsPresenter {

    private final UsersRepository mUsersRepository;
    private final UsersContract.View mView;

    public UsersPresenter(UsersRepository usersRepository, UsersContract.View view) {
        mUsersRepository = Utils.checkNotNull(usersRepository, "usersRepository can not be null");
        mView = Utils.checkNotNull(view, "view can not be null");

        mView.setPresenter(this);
    }

    @Override
    public void getUsers(String param, int page) {
        if (param == null) {
            return;
        }

        param = param.trim();

        if (param.isEmpty()) {
            return;
        }

//        mView.onPreLoad();
        cancelRequest();

        mUsersRepository.getUsers(new UserDataSource.FetchUsersCallback() {
            @Override
            public void onUsersFetched(ResUserData users) {
                List<UserModel> newRes = new ResToUiUserList().convert(users.getData());
                mView.onUsersFetched(newRes);
            }

            @Override
            public void onDataNotAvailable() {
                mView.onUsersFetchFailure();
            }
        }, param, page);
    }

    @Override
    public void cancelRequest() {
        mUsersRepository.cancelRequest();
    }
}