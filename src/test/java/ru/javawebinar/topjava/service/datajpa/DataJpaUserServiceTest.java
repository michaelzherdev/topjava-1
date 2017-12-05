package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.UserTestData.*;

/**
 * Created by mikhail on 05.12.17.
 */
@ActiveProfiles(resolver = ActiveDbDataJpaProfileResolver.class)
public class DataJpaUserServiceTest extends UserServiceTest {


    @Test
    public void getWithMeal() throws Exception {
        User user = service.getWithMeal(ADMIN_ID);
        assertMatch(user, ADMIN);
        MealTestData.assertMatch(user.getMeals(), MealTestData.ADMIN_MEAL1, MealTestData.ADMIN_MEAL2);
    }

    @Test(expected = NotFoundException.class)
    public void getWithMealNotFound() throws Exception {
        service.getWithMeal(1);
    }

}
