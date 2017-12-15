package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

/**
 * Created by mikhail on 14.12.17.
 */
@Controller
public class MealController {

    @Autowired
    private MealService service;

    @GetMapping("/meals")
    public String getAll(HttpServletRequest request, Model model) {
        String action = request.getParameter("action");
        int userId = AuthorizedUser.id();

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                service.delete(id, userId);
                model.addAttribute("meals", MealsUtil.getWithExceeded(service.getAll(userId), AuthorizedUser.getCaloriesPerDay()));
                return "meals";
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        service.get(getId(request), userId);
                model.addAttribute("meal", meal);
                return "mealForm";
            case "all":
            default:
                model.addAttribute("meals", MealsUtil.getWithExceeded(service.getAll(userId), AuthorizedUser.getCaloriesPerDay()));
                return "meals";
        }
    }
    
    @PostMapping("/meals")
    public String createMeal(HttpServletRequest request, Model model) {
        String action = request.getParameter("action");
        int userId = AuthorizedUser.id();

        if (action == null) {
            Meal meal = new Meal(
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            if (request.getParameter("id").isEmpty()) {
                service.create(meal, userId);
            } else {
                meal.setId(Integer.valueOf(request.getParameter("id")));
                service.update(meal, userId);
            }
            model.addAttribute("meals", MealsUtil.getWithExceeded(service.getAll(userId), AuthorizedUser.getCaloriesPerDay()));
        } else if ("filter".equals(action)) {
            LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
            LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
            LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
            LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

            List<Meal> mealsDateFiltered = service.getBetweenDates(
                    startDate != null ? startDate : DateTimeUtil.MIN_DATE,
                    endDate != null ? endDate : DateTimeUtil.MAX_DATE, userId);

            List<MealWithExceed> mealWithExceeds = MealsUtil.getFilteredWithExceeded(mealsDateFiltered,
                    startTime != null ? startTime : LocalTime.MIN,
                    endTime != null ? endTime : LocalTime.MAX,
                    AuthorizedUser.getCaloriesPerDay());

            model.addAttribute("meals", mealWithExceeds);
        }
        return "meals";
    }
    
    @GetMapping("/mealForm")
    public String mealForm(HttpServletRequest request, @RequestBody Meal meal, Model model) {
        model.addAttribute("meals", service.getAll(AuthorizedUser.id()));
        return "redirect:meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
