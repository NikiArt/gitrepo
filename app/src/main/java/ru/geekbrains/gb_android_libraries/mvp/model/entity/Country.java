package ru.geekbrains.gb_android_libraries.mvp.model.entity;

public class Country
{
    String name;
    String code;
    Long space;

    public Country(String name, String code)
    {
        this.name = name;
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public String getCode()
    {
        return code;
    }

    public Long getSpace()
    {
        return space;
    }

    public void setSpace(Long space)
    {
        this.space = space;
    }
}
