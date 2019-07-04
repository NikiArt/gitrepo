package ru.geekbrains.gb_android_libraries.mvp.model.api;


import java.util.List;

import io.reactivex.Single;
import retrofit2.http.*;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.Repository;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.User;

public interface IDataSource {
    @GET("/users/{user}")
    Single<User> getUser(@Path("user") String username);

//    @GET
//    Single<List<Repository>> getUserRepos(@Url String url);

    @GET("/users/{user}/repos")
    Single<List<Repository>> getRepo(@Path("user") String username);
}
