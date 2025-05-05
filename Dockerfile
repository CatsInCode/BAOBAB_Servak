# Сначала билдим проект
FROM gradle:8.2.1-jdk17 AS builder
COPY . /app
WORKDIR /app
RUN gradle installDist

# Затем собираем финальный контейнер
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/build/install/servak /app
CMD ["./bin/servak"]
