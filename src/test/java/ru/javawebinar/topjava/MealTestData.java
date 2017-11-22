package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

/**
 * Created by mikhail on 22.11.17.
 */
public class MealTestData {

    public static final int MEAL_1_ID = START_SEQ + 2;
    public static final int MEAL_2_ID = START_SEQ + 3;
    public static final int MEAL_3_ID = START_SEQ + 4;

    public static final Meal MEAL_1 = new Meal(MEAL_1_ID, LocalDateTime.of(2017, 11, 22, 10, 36, 38), "breakfast", 500);
    public static final Meal MEAL_2 = new Meal(MEAL_2_ID, LocalDateTime.of(2017, 11, 22, 14, 36, 38), "lunch", 1000);
    public static final Meal MEAL_3 = new Meal(MEAL_3_ID, LocalDateTime.of(2017, 11, 22, 19, 36, 38), "dinner", 500);

    public static final LocalDateTime START_DATE_TIME = LocalDateTime.of(2017, 11, 22, 10, 00, 00);
    public static final LocalDateTime END_DATE_TIME = LocalDateTime.of(2017, 11, 22, 19, 00, 00);

    public static final LocalDate START_DATE = LocalDate.of(2017, 11, 22);
    public static final LocalDate END_DATE = LocalDate.of(2017, 11, 22);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
