package ru.javawebinar.topjava.service;

import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * Created by mikhail on 22.11.17.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() throws Exception {
        Meal meal = service.get(MEAL_1_ID, USER_ID);
        assertMatch(meal, MEAL_1);
    }

    @Test
    public void delete() throws Exception {
        service.delete(MEAL_1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL_3, MEAL_2);
    }

    @Test
    public void getBetweenDates() throws Exception {
        List<Meal> all = service.getBetweenDates(START_DATE, END_DATE, USER_ID);
        assertMatch(all, MEAL_3, MEAL_2, MEAL_1);
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
        List<Meal> all = service.getBetweenDateTimes(START_DATE_TIME, END_DATE_TIME, USER_ID);
        assertMatch(all, MEAL_2, MEAL_1);
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, MEAL_3, MEAL_2, MEAL_1);
    }

    @Test(expected = ComparisonFailure.class)
    public void getAllWrongOrder() throws Exception {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, MEAL_1, MEAL_2, MEAL_3);
    }

    @Test
    public void update() throws Exception {
        Meal updated = new Meal(MEAL_1);
        updated.setDescription("updated breakfast");
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL_1_ID, USER_ID), updated);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = new Meal(null, LocalDateTime.of(2017, 11, 22, 21, 36, 36), "tea", 200);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(USER_ID), newMeal, MEAL_3, MEAL_2, MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void getAlien() throws Exception {
        Meal meal = service.get(MEAL_1_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAlien() throws Exception {
        service.delete(MEAL_1_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateAlien() throws Exception {
        Meal updated = new Meal(MEAL_1);
        updated.setDescription("updated breakfast");
        service.update(updated, ADMIN_ID);
    }

}