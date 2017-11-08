package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

/**
 * Created by mikhail on 07.11.17.
 */
public interface MealDao {

    List<Meal> getAll();

    Meal getById(int id);

    Meal add(Meal meal);

    Meal update(Meal meal);

    Meal delete(int id);
}
