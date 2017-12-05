package ru.javawebinar.topjava.service.jpa;

import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;

import static ru.javawebinar.topjava.Profiles.JPA;

public class ActiveDbJpaProfileResolver extends ActiveDbProfileResolver {

    @Override
    public String[] resolve(Class<?> aClass) {
        return new String[]{Profiles.getActiveDbProfile(), JPA};
    }
}