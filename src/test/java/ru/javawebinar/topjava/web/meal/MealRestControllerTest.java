package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * Created by mikhail on 25.12.17.
 */
public class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';
    private DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    @Autowired
    MealService mealService;


    @Test
    public void testGetAll() throws Exception {
        TestUtil.print(mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL1, MEAL2, MEAL3, MEAL4, MEAL5, MEAL6)));
    }

    @Test
    public void testGet() throws Exception {
        TestUtil.print(
                mockMvc.perform(get(REST_URL + MEAL1_ID))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(contentJson(MEAL1))
        );
    }

    @Test
    public void testCreateWithLocation() throws Exception {
        Meal expected = getCreated();
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Meal returned = TestUtil.readFromJson(action, Meal.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        MealTestData.assertMatch(mealService.getAll(USER_ID), expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andExpect(status().isOk());
        assertMatch(mealService.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = getUpdated();
        mockMvc.perform(put(REST_URL + MEAL1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isOk());

        assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void testGetBetween() throws Exception {
        LocalDateTime startDateTime = LocalDateTime.of(2015, Month.MAY, 31, 9, 15, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2015, Month.MAY, 31, 15, 15, 0);
        String url = REST_URL + String.format("filter?startDate=%s&endDate=%s",
                formatter.format(startDateTime), formatter.format(endDateTime));

        TestUtil.print(mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(contentJson(MEAL4, MEAL5)));
    }

    @Test
    public void testGetBetweenOptional() throws Exception {
        LocalDateTime startDateTime = LocalDateTime.of(2015, Month.MAY, 31, 9, 15, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2015, Month.MAY, 31, 15, 15, 0);
        String url = REST_URL + String.format("between?startDate=%s&endDate=%s&startTime=%s&endTime=%s",
                startDateTime.toLocalDate().toString(),
                endDateTime.toLocalDate().toString(),
                startDateTime.toLocalTime().toString(),
                endDateTime.toLocalTime().toString());

        TestUtil.print(mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL4, MEAL5)));
    }

    @Test
    public void testGetBetweenOptionalNull() throws Exception {
        LocalDateTime startDateTime = LocalDateTime.of(2015, Month.MAY, 31, 9, 15, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2015, Month.MAY, 31, 15, 15, 0);
        String url = REST_URL + String.format("between?startDate=%s&endDate=%s&startTime=&endTime=",
                startDateTime.toLocalDate().toString(),
                endDateTime.toLocalDate().toString());

        TestUtil.print(mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL4, MEAL5, MEAL6)));
    }

}