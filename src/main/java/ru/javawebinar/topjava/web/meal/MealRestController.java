package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.*;

@RestController
@RequestMapping(MealRestController.REST_URL)
public class MealRestController extends AbstractMealController {

    static final String REST_URL = "/rest/meals";

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Meal get(@PathVariable("id") int id) {
        return super.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@RequestBody Meal meal) {
        Meal created = super.create(meal);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody Meal meal, @PathVariable("id") int id) {
        super.update(meal, id);
    }

    @GetMapping(value = "/filter", params = {"startDate", "endDate"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getBetween(@DateTimeFormat(iso = DATE_TIME) @RequestParam("startDate") LocalDateTime startDateTime,
                                           @DateTimeFormat(iso = DATE_TIME)  @RequestParam("endDate") LocalDateTime endDateTime ) {
        return super.getBetween(startDateTime.toLocalDate(), startDateTime.toLocalTime(),
                                endDateTime.toLocalDate(), endDateTime.toLocalTime());
    }

    @GetMapping(value = "/between", params = {"startDate", "endDate", "startTime", "endTime"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getBetweenOptional(@RequestParam(name = "startDate", required = false) LocalDate startDate,
                                            @RequestParam(name = "endDate", required = false) LocalDate endDate,
                                            @RequestParam(name = "startTime", required = false) LocalTime startTime,
                                            @RequestParam(name = "endTime", required = false) LocalTime endTime      ) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}