package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.MealServiceTest;

/**
 * Created by mikhail on 05.12.17.
 */
@ActiveProfiles(resolver = ActiveDbJpaProfileResolver.class)
public class JpaMealServiceTest extends MealServiceTest {
}
