# Sử dụng OpenJDK 17
FROM openjdk:17-jdk-slim

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file JAR vào container
COPY target/*.jar app.jar

# Expose port 8081
EXPOSE 8081

# Lệnh chạy ứng dụng
CMD ["java", "-jar", "app.jar"]
