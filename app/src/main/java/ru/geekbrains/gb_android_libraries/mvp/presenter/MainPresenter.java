package ru.geekbrains.gb_android_libraries.mvp.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.Repository;
import ru.geekbrains.gb_android_libraries.mvp.model.repo.CountriesRepo;
import ru.geekbrains.gb_android_libraries.mvp.model.repo.UsersRepo;
import ru.geekbrains.gb_android_libraries.mvp.view.CountryRowView;
import ru.geekbrains.gb_android_libraries.mvp.view.MainView;
import timber.log.Timber;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    @SuppressLint("CheckResult")
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
        loadCountries();
        loadUser();

        countriesListPresenter.getClickSubject().subscribe(countryRowView -> {
            getViewState().showMessage(countriesListPresenter.repositories.get(countryRowView.getPos()).getName());
        });
    }

    private CountriesRepo coutriesRepo;
    private UsersRepo usersRepo;
    private Scheduler mainThreadScheduler;
    private CountriesListPresenter countriesListPresenter;

    public MainPresenter(Scheduler mainThreadScheduler) {
        this.coutriesRepo = new CountriesRepo();
        this.usersRepo = new UsersRepo();
        this.mainThreadScheduler = mainThreadScheduler;
        countriesListPresenter = new CountriesListPresenter();
    }

    public ICountryListPresenter getCountriesListPresenter() {
        return countriesListPresenter;
    }

    class CountriesListPresenter implements ICountryListPresenter {

        PublishSubject<CountryRowView> clickSubject = PublishSubject.create();
        List<Repository> repositories = new ArrayList<>();

        @Override
        public void bind(CountryRowView view) {
            view.setTitle(repositories.get(view.getPos()).getName());
            view.setCode("(" + repositories.get(view.getPos()).getId() + ")");
        }

        @Override
        public int getCount() {
            return repositories.size();
        }

        @Override
        public PublishSubject<CountryRowView> getClickSubject() {
            return clickSubject;
        }
    }


    private void loadCountries() {

        getViewState().showLoading();
        usersRepo.getRepo("googlesamples")
                .observeOn(mainThreadScheduler)
                .subscribe(repositories -> {

                    //@Url usersRepo.getUserRepositories(user.getReposUrl()).subscribe....
                    countriesListPresenter.repositories.clear();
                    countriesListPresenter.repositories.addAll(repositories);
                    getViewState().updateList();
                    getViewState().hideLoading();
                }, t -> {
                    getViewState().hideLoading();
                    Timber.e(t);
                });
    }

    private void loadUser() {
        getViewState().showLoading();
        usersRepo.getUser("googlesamples")
                .observeOn(mainThreadScheduler)
                .subscribe(user -> {

                    //@Url usersRepo.getUserRepositories(user.getReposUrl()).subscribe....


                    getViewState().hideLoading();
                    getViewState().setUsername(user.getLogin());
                    getViewState().loadImage(user.getAvatarUrl());
                }, t -> {
                    getViewState().hideLoading();
                    Timber.e(t);
                });
    }

    @SuppressLint("CheckResult")
    private void loadDataWithOkHttp() {
        Single<String> single = Single.fromCallable(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.github.com/users/googlesamples")
                    .build();

            return client.newCall(request).execute().body().string();
        });

        single.subscribeOn(Schedulers.io())
                .subscribe(string -> Timber.d(string));
    }
}
