package com.android.anmol.githubapi.base;

import com.android.anmol.githubapi.data.source.UsersRepository;
import com.android.anmol.githubapi.data.source.remote.UsersRemoteDataSource;

/**
 * Injects various components throughout the application.
 */
public class Injection {

    /**
     * Provides the repository which handles the Network Calls, Databases etc anything.
     *
     * @return The Users Repository.
     */
    public static UsersRepository provideUsersRepository() {
        return UsersRepository.getInstance(UsersRemoteDataSource.getInstance());
    }
}
