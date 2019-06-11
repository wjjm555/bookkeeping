package com.qinggan.mybookkeepingapplication.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

public class MemberConvert implements PropertyConverter<List<Integer>, String> {

    private Gson gson = new Gson();

    @Override
    public List<Integer> convertToEntityProperty(String databaseValue) {
        return gson.fromJson(databaseValue, new TypeToken<List<Integer>>() {
        }.getType());
    }

    @Override
    public String convertToDatabaseValue(List<Integer> entityProperty) {
        return gson.toJson(entityProperty);
    }
}
