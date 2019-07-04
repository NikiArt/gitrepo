package ru.geekbrains.gb_android_libraries.mvp.model.api;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.Repository;

public interface IRepoSource {

        @GET("/users/{user}/repos")
        Single<List<Repository>> getRepo(@Path("user") String username);
}
