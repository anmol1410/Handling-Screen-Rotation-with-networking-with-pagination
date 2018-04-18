package com.android.anmol.githubapi;

import java.util.List;

class UsersPresenter implements UsersContract.FeedDetailsPresenter {

    private final UsersRepository mUsersRepository;
    private final UsersContract.View mView;

    public UsersPresenter(UsersRepository usersRepository, UsersContract.View view) {
        mUsersRepository = Utils.checkNotNull(usersRepository, "usersRepository can not be null");
        mView = Utils.checkNotNull(view, "view can not be null");

        mView.setPresenter(this);
    }

    @Override
    public void getUsers(String param) {
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
        }, param);
    }

    @Override
    public void cancelRequest() {

    }
}