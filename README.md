При первом запуске создаются 2 юзера:

login: "admin@gmail.com", password: "admin" (роли: ROLE_USER, ROLE_ADMIN)

login: "user@gmail.com", password: "user" (роли: ROLE_USER)

Cервер localhost:8080

Все CRUD-операции и страницы для них доступны только пользователю с ролью admin по url: /admin
Данные авторизованного пользователя находятся по url: /user
