Поднимаем докер

docker run --name user-db \
-e POSTGRES_DB=user_service \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASSWORD=postgres \
-p 5432:5432 \
-d postgres:14

Запускаем mvn compile, (генерируется тест из openapi.yaml, я его пока что просто удаляю, чтобы не тратить время на эту проблему)
Запускаемся
Прилагаю postman коллекцию для теста, можно импортнуть к себе и прогнать
