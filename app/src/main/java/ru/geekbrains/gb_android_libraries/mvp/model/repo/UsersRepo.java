package ru.geekbrains.gb_android_libraries.mvp.model.repo;


import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.geekbrains.gb_android_libraries.mvp.model.api.ApiHolder;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.Repository;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.User;

public class UsersRepo {
    public Single<User> getUser(String username) {
        return ApiHolder.getApi().getUser(username).subscribeOn(Schedulers.io());
    }

    public Single<List<Repository>> getRepo(String username) {
        return ApiHolder.getApi().getRepo(username).subscribeOn(Schedulers.io());
    }
}
