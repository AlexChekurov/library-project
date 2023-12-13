# Сборка и запуск приложения
## Старт приложения
В корне проекта выполняем последовательно перечисленные ниже команды.

Сборка проекта
```sh
mvn clean install
```

Сборка докер образа основного модуля
```sh
docker build -t library:0.1.0 ./library 
```

Сборка докер образа вспомогательного модуля
```sh
docker build -t library-service:0.1.0 ./library-service 
```

Останавливаем ранее запущенный compose-файл
```shell
docker compose down
```

Запускаем compose-файл
```shell
docker compose up -d
```

Открываем сваггер приложения по ссылке: http://localhost:8080/swagger-ui.html, 
по ссылке: http://localhost:8082/swagger-ui.html доступен сваггер library-service

Логины и пароли:

| LOGIN   |   PASSWORD   |          SERVICE          | GRANT       |
|---------|:------------:|:-------------------------:|-------------|
| user    |  user_pass   |           main            | USER        |
| admin   |  admin_pass  |           main            | USER, ADMIN |
| user_1  | user_pass_1  |      library-sefvice      | USER        |
| admin_1 | admin_pass_1 |      library-sefvice      | USER, ADMIN |
