package ru.javawebinar.topjava.service.datajpa;

import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;

import static ru.javawebinar.topjava.Profiles.DATAJPA;

public class ActiveDbDataJpaProfileResolver extends ActiveDbProfileResolver {

    @Override
    public String[] resolve(Class<?> aClass) {
        return new String[]{Profiles.getActiveDbProfile(), DATAJPA};
    }
}