package com.android.anmol.githubapi.user_feeds.presenter;

import com.android.anmol.githubapi.data.source.ResUserData;
import com.android.anmol.githubapi.data.source.UserDataSource;
import com.android.anmol.githubapi.data.source.UsersRepository;
import com.android.anmol.githubapi.user_feeds.UserModel;
import com.android.anmol.githubapi.utility.Utils;
import com.android.anmol.githubapi.utility.converter.ResToUiUserList;

import java.util.List;

/**
 * Listens to user actions from the UI ({@link com.android.anmol.githubapi.user_feeds.SearchActivity}),
 * retrieves the data and updates the UI as required.
 */
public class UsersPresenter implements UsersContract.FeedDetailsPresenter {

    /**
     * Repository to fetch the data from.
     */
    private final UsersRepository mUsersRepository;

    /**
     * View to communicate back to the client.
     */
    private final UsersContract.View mView;

    public UsersPresenter(UsersRepository usersRepository, UsersContract.View view) {
        mUsersRepository = Utils.checkNotNull(usersRepository, "usersRepository can not be null");
        mView = Utils.checkNotNull(view, "view can not be null");

        mView.setPresenter(this);
    }

    @Override
    public void getUsers(String param, int page) {
        // Process the input.
        if (param == null) {
            return;
        }

        param = param.trim();

        if (param.isEmpty()) {
            return;
        }

        if (page == 0) {
            // Means data loaded for first page for the given query.
            mView.onPreLoad();
        }

        // Cancel the outgoing request if any, before making the new one.
        cancelRequest();

        // Ask repository to provide the data.
        mUsersRepository.getUsers(new UserDataSource.FetchUsersCallback() {
            @Override
            public void onUsersFetched(ResUserData users) {
                // Process network response into the response UI can understand.
                List<UserModel> newRes = new ResToUiUserList().convert(users.getData());

                // Send back the result.
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