FROM maven:3.9.12-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENV DB_HOST=localhost
ENV DB_PORT=1433
ENV DB_NAME=elder_care_21_jul
ENV DB_USERNAME=sa


# Các cấu hình connection pool (Nếu không khai báo, Spring Boot sẽ tự lấy giá trị mặc định sau dấu hai chấm)
ENV DB_POOL_NAME=EldCareHikariCP
ENV DB_MIN_IDLE=5
ENV DB_MAX_POOL_SIZE=20
ENV DB_IDLE_TIMEOUT=300000
ENV DB_MAX_LIFETIME=1200000
ENV DB_CONNECTION_TIMEOUT=20000
ENV DB_VALIDATION_TIMEOUT=5000

# Cấu hình CORS
ENV ALLOWED_ORIGINS=http://localhost:5173,http://localhost:3000

ENTRYPOINT ["java", "-jar", "app.jar"]