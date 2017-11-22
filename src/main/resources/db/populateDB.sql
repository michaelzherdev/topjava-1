DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories) VALUES
  (100000, TIMESTAMP '2017-11-22 10:36:38', 'breakfast', 500),
  (100000, TIMESTAMP '2017-11-22 14:36:38', 'lunch', 1000),
  (100000, TIMESTAMP '2017-11-22 19:36:38', 'dinner', 500);
