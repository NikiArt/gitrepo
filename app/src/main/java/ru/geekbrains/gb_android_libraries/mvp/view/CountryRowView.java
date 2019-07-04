package ru.geekbrains.gb_android_libraries.mvp.view;

import android.support.annotation.NonNull;

public interface CountryRowView {
    int getPos();
    void setTitle(@NonNull String title);
    void setCode(@NonNull String code);
}
