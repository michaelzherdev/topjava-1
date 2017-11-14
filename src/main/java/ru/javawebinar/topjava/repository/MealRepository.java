package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {
    Meal save(Meal meal, int userId);

    void delete(int id, int userId);

    Meal get(int id, int userId);

    List<Meal> getAllByUser(int userId);

    List<Meal> getBetween(int userId, LocalDateTime startTime, LocalDateTime endTime);

    List<Meal> getAll();
}
