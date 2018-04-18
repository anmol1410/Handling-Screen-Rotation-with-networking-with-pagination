package com.android.anmol.githubapi;

public class Injection {

    public static UsersRepository provideUsersRepository() {
        return UsersRepository.getInstance(UsersRemoteDataSource.getInstance());
    }
}
