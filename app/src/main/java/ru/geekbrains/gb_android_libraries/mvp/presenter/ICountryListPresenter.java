package ru.geekbrains.gb_android_libraries.mvp.presenter;

import io.reactivex.subjects.PublishSubject;
import ru.geekbrains.gb_android_libraries.mvp.view.CountryRowView;

public interface ICountryListPresenter {
    void bind(CountryRowView view);
    int getCount();
    PublishSubject<CountryRowView> getClickSubject();
}
