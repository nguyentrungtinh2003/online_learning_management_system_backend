package com.TrungTinhBackend.codearena_backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class CodearenaBackendApplication {

	public static void main(String[] args) throws IOException {
		Dotenv dotenv = Dotenv.load();

		// Đọc các biến từ file .env và thiết lập chúng vào hệ thống
		System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL"));
		System.setProperty("DATABASE_USERNAME", dotenv.get("DATABASE_USERNAME"));
		System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD"));

		System.setProperty("CLOUDINARY_API_KEY", dotenv.get("CLOUDINARY_API_KEY"));
		System.setProperty("CLOUDINARY_API_SECRET", dotenv.get("CLOUDINARY_API_SECRET"));
		System.setProperty("CLOUDINARY_NAME", dotenv.get("CLOUDINARY_NAME"));

		System.setProperty("MAIL_HOST", dotenv.get("MAIL_HOST"));
		System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
		System.setProperty("MAIL_PORT", dotenv.get("MAIL_PORT"));
		System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));

		System.setProperty("GOOGLE_AUTHORIZATION_URI", dotenv.get("GOOGLE_AUTHORIZATION_URI"));
		System.setProperty("GOOGLE_TOKEN_URI", dotenv.get("GOOGLE_TOKEN_URI"));
		System.setProperty("GOOGLE_USER_INFO_URI", dotenv.get("GOOGLE_USER_INFO_URI"));
		System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
		System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
		System.setProperty("GOOGLE_REDIRECT_URI", dotenv.get("GOOGLE_REDIRECT_URI"));
		System.setProperty("GOOGLE_SCOPE", dotenv.get("GOOGLE_SCOPE"));

		System.setProperty("PAYPAL_CLIENT_ID", dotenv.get("PAYPAL_CLIENT_ID"));
		System.setProperty("PAYPAL_CLIENT_SECRET", dotenv.get("PAYPAL_CLIENT_SECRET"));
		System.setProperty("PAYPAL_MODE", dotenv.get("PAYPAL_MODE"));

		SpringApplication.run(CodearenaBackendApplication.class, args);
	}
}
