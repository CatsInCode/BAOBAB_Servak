# Сначала билдим проект
FROM gradle:8.4-jdk17 AS builder
COPY . /app
WORKDIR /app

# Устанавливаем xargs
RUN apt-get update && apt-get install -y findutils

RUN gradle installDist

# Затем собираем финальный контейнер
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/build/install/servak /app
CMD ["./bin/servak"]
