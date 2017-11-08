package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by mikhail on 07.11.17.
 */
public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);

    MealDao mealDao;

    public MealServlet() {
        mealDao = new MealDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        List<Meal> meals = mealDao.getAll();
        List<MealWithExceed> mealsWithExceeded = MealsUtil.getFilteredWithExceeded(meals, LocalTime.MIN, LocalTime.MAX, 2000);
        request.setAttribute("meals", mealsWithExceeded);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        log.debug("post {} request", action);
        switch (action) {
            case "new":
                request.getRequestDispatcher("mealForm.jsp").forward(request, response);
                break;
            case "edit":
                int id = Integer.parseInt(request.getParameter("id"));
                Meal existingMeal = mealDao.getById(id);
                RequestDispatcher dispatcher = request.getRequestDispatcher("mealForm.jsp");
                request.setAttribute("meal", existingMeal);
                dispatcher.forward(request, response);
                break;
            case "insert":
            case "update":
                saveMeal(request, response);
                break;
            case "delete":
                deleteMeal(request, response);
                break;
            default:
                doGet(request, response);
                break;
        }
    }

    private void deleteMeal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        mealDao.delete(Integer.parseInt(id));
        log.debug("deleted meal id {}", id);
        response.sendRedirect("meals");
    }

    private void saveMeal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String strId = request.getParameter("id");
        String dateTime = request.getParameter("datetime");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
        String description = request.getParameter("description");
        String calories = request.getParameter("calories");

        if(strId == null || strId.isEmpty()) {
            Meal meal = new Meal(localDateTime ,description, Integer.parseInt(calories));
            mealDao.add(meal);
            log.debug("{} inserted", meal);
        } else {
            Integer id = Integer.parseInt(strId);
            Meal meal = new Meal(id, localDateTime,description, Integer.parseInt(calories));
            mealDao.update(meal);
            log.debug("{} updated", meal);
        }
        response.sendRedirect("meals");
    }
}
