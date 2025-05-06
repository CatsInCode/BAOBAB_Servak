FROM gradle:8.4-jdk17 AS builder
COPY . /app
WORKDIR /app
RUN gradle installDist

FROM ubuntu:22.04
WORKDIR /app
RUN apt-get update && apt-get install -y openjdk-17-jdk  # Устанавливаем JDK
COPY --from=builder /app/build/install/servak /app
CMD ["./bin/servak"]