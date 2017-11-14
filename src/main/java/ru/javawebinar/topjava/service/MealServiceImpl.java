package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository repository;

    @Override
    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        repository.delete(id, userId);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        Meal meal = repository.get(id, userId);
        if(Objects.isNull(meal))
            throw new NotFoundException("Meal for given user not found");
        return meal;
    }

    @Override
    public void update(Meal meal, int userId) {
        repository.save(meal, userId);
    }

    @Override
    public List<Meal> getAllByUser(int userId) {
        return repository.getAllByUser(userId);
    }

    @Override
    public List<Meal> getBetween(int userId, LocalDateTime startTime, LocalDateTime endTime) {
        return repository.getBetween(userId, startTime, endTime);
    }

    @Override
    public List<Meal> getAll() {
        return repository.getAll();
    }
}