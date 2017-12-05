package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.MealServiceTest;

/**
 * Created by mikhail on 05.12.17.
 */
@ActiveProfiles(resolver = ActiveDbJdbcProfileResolver.class)
public class JdbcMealServiceTest extends MealServiceTest {
}
