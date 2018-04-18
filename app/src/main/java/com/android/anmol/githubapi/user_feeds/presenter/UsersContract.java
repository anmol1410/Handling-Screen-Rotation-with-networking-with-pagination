package com.android.anmol.githubapi.user_feeds.presenter;

import com.android.anmol.githubapi.base.BaseView;
import com.android.anmol.githubapi.user_feeds.UserModel;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface UsersContract {

    interface View extends BaseView<FeedDetailsPresenter> {

        void onUsersFetched(List<UserModel> users);

        void onUsersFetchFailure();

        void onPreLoad();
    }

    interface FeedDetailsPresenter {

        void getUsers(String param, int page);

        void cancelRequest();
    }
}
