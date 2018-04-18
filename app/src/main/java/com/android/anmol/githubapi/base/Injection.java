package com.android.anmol.githubapi.base;

import com.android.anmol.githubapi.data.source.UsersRepository;
import com.android.anmol.githubapi.data.source.remote.UsersRemoteDataSource;

public class Injection {

    public static UsersRepository provideUsersRepository() {
        return UsersRepository.getInstance(UsersRemoteDataSource.getInstance());
    }
}
