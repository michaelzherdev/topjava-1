package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by mikhail on 07.11.17.
 */
public class MealDaoImpl implements MealDao {

    private static Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static AtomicInteger syncId;

    static {
        List<Meal> mealList = Arrays.asList(
                new Meal(1, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(2, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(3, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(4, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(5, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(6, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        meals = mealList.stream().collect(Collectors.toMap(Meal::getId, Function.identity()));
        syncId = new AtomicInteger(meals.size());
    }

    @Override
    public List<Meal> getAll() {
        return meals.values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public Meal getById(int id) {
        return meals.get(id);
    }

    @Override
    public Meal add(Meal meal) {
        meal.setId(syncId.incrementAndGet());
        return meals.put(syncId.get(), meal);
    }

    @Override
    public Meal update(Meal meal) {
        return meals.put(meal.getId(), meal);
    }

    @Override
    public Meal delete(int id) {
        return meals.remove(id);
    }
}
