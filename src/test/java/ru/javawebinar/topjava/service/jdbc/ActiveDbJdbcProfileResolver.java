package ru.javawebinar.topjava.service.jdbc;

import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;

import static ru.javawebinar.topjava.Profiles.JDBC;

public class ActiveDbJdbcProfileResolver extends ActiveDbProfileResolver {

    @Override
    public String[] resolve(Class<?> aClass) {
        return new String[]{Profiles.getActiveDbProfile(), JDBC};
    }
}