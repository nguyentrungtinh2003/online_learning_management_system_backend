# Bước 1: Dùng image có Maven + JDK để build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy toàn bộ source code vào container
COPY . .

# Chạy lệnh build Maven
RUN mvn clean package -DskipTests

# Bước 2: Dùng image nhẹ hơn để chạy app
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy file JAR từ bước build trước
COPY --from=build /app/target/*.jar app.jar

# Mở port 8081
EXPOSE 8081

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
