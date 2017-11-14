package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(m -> save(m, AuthorizedUser.id()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
        }
        if(meal.getUserId() == userId)
            repository.put(meal.getId(), meal);
        else
            throw new UnsupportedOperationException("Meal don`t belong to current user.");
        return meal;
    }

    @Override
    public void delete(int id, int userId) {
        Meal meal = repository.get(id);
        if(meal.getUserId() == userId)
            repository.remove(id);
        else
            throw new UnsupportedOperationException("Meal don`t belong to current user.");
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        return (userId == meal.getUserId()) ? meal : null;
    }

    @Override
    public List<Meal> getAllByUser(int userId) {
        return repository.values().stream()
                .filter(m -> m.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetween(int userId, LocalDateTime startTime, LocalDateTime endTime) {
        return repository.values().stream()
                .filter(m -> m.getUserId() == userId)
                .filter( m -> DateTimeUtil.isBetween((m.getDateTime()), startTime, endTime))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll() {
        return repository.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

