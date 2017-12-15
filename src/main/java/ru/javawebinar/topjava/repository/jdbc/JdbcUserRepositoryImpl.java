package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Repository
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final RowMapper<Role> ROLE_ROW_MAPPER = ((resultSet, i) -> Role.valueOf(resultSet.getString("role")));

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());

            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    List<Role> roles = user.getRoles().stream().collect(toList());
                    preparedStatement.setInt(1, user.getId());
                    preparedStatement.setString(2, roles.get(i).name());
                }

                @Override
                public int getBatchSize() {
                    return user.getRoles().size();
                }
            });
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        return user;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return addRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return addRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM user_roles r, users u WHERE u.id = r.user_id ORDER BY u.name, u.email", resultSet -> {
            Map<Integer, User> users = new LinkedHashMap<>();
            while (resultSet.next()) {
                for(int i = 1; i < resultSet.getMetaData().getColumnCount(); i++) {
                    Integer userId = resultSet.getInt(1);
                    User userInMap = users.get(userId);
                    if(Objects.isNull(userInMap)) {
                        User user = new User();
                        user.setId(resultSet.getInt(3));
                        user.setName(resultSet.getString(4));
                        user.setEmail(resultSet.getString(5));
                        user.setPassword(resultSet.getString(6));
                        user.setRegistered(resultSet.getDate(7));
                        user.setEnabled(resultSet.getBoolean(8));
                        user.setCaloriesPerDay(resultSet.getInt(9));

                        Role role = Role.valueOf(resultSet.getString(2));
                        user.setRoles(Collections.singleton(role));
                        users.put(userId, user);
                    } else {
                        userInMap.getRoles().add(Role.valueOf(resultSet.getString(2)));
                    }

                }
            }
            return users.values().stream().collect(toList());
        });
    }

    private User addRoles(User user) {
        if(Objects.nonNull(user)){
            List<Role> roles = jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?", ROLE_ROW_MAPPER, user.getId());
            user.setRoles(roles);
        }
        return user;
    }
}
