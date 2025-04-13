package com.TrungTinhBackend.codearena_backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class CodearenaBackendApplication {

	public static void main(String[] args) throws IOException {
//		Dotenv dotenv = Dotenv.load();
//
//		// Đọc các biến từ file .env và thiết lập chúng vào hệ thống
//		System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL"));
//		System.setProperty("DATABASE_USERNAME", dotenv.get("DATABASE_USERNAME"));
//		System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD"));
//		System.setProperty("SERVER.PORT", dotenv.get("SERVER.PORT"));
//
//		System.setProperty("CLOUDINARY_API_KEY", dotenv.get("CLOUDINARY_API_KEY"));
//		System.setProperty("CLOUDINARY_API_SECRET", dotenv.get("CLOUDINARY_API_SECRET"));
//		System.setProperty("CLOUDINARY_NAME", dotenv.get("CLOUDINARY_NAME"));
//
//		System.setProperty("MAIL_HOST", dotenv.get("MAIL_HOST"));
//		System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
//		System.setProperty("MAIL_PORT", dotenv.get("MAIL_PORT"));
//		System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
//
//		System.setProperty("GOOGLE_AUTHORIZATION_URI", dotenv.get("GOOGLE_AUTHORIZATION_URI"));
//		System.setProperty("GOOGLE_TOKEN_URI", dotenv.get("GOOGLE_TOKEN_URI"));
//		System.setProperty("GOOGLE_USER_INFO_URI", dotenv.get("GOOGLE_USER_INFO_URI"));
//		System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
//		System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
//		System.setProperty("GOOGLE_REDIRECT_URI", dotenv.get("GOOGLE_REDIRECT_URI"));
//		System.setProperty("GOOGLE_SCOPE", dotenv.get("GOOGLE_SCOPE"));
//
//		System.setProperty("PAYPAL_CLIENT_ID", dotenv.get("PAYPAL_CLIENT_ID"));
//		System.setProperty("PAYPAL_CLIENT_SECRET", dotenv.get("PAYPAL_CLIENT_SECRET"));
//		System.setProperty("PAYPAL_MODE", dotenv.get("PAYPAL_MODE"));
//----------------------------
		System.setProperty("DATABASE_URL", System.getenv("DATABASE_URL"));
		System.setProperty("DATABASE_USERNAME", System.getenv("DATABASE_USERNAME"));
		System.setProperty("DATABASE_PASSWORD", System.getenv("DATABASE_PASSWORD"));
		System.setProperty("SERVER.PORT", System.getenv("SERVER.PORT"));

		System.setProperty("CLOUDINARY_API_KEY", System.getenv("CLOUDINARY_API_KEY"));
		System.setProperty("CLOUDINARY_API_SECRET", System.getenv("CLOUDINARY_API_SECRET"));
		System.setProperty("CLOUDINARY_NAME", System.getenv("CLOUDINARY_NAME"));

		System.setProperty("MAIL_HOST", System.getenv("MAIL_HOST"));
		System.setProperty("MAIL_PASSWORD", System.getenv("MAIL_PASSWORD"));
		System.setProperty("MAIL_PORT", System.getenv("MAIL_PORT"));
		System.setProperty("MAIL_USERNAME", System.getenv("MAIL_USERNAME"));

		System.setProperty("GOOGLE_AUTHORIZATION_URI", System.getenv("GOOGLE_AUTHORIZATION_URI"));
		System.setProperty("GOOGLE_TOKEN_URI", System.getenv("GOOGLE_TOKEN_URI"));
		System.setProperty("GOOGLE_USER_INFO_URI", System.getenv("GOOGLE_USER_INFO_URI"));
		System.setProperty("GOOGLE_CLIENT_ID", System.getenv("GOOGLE_CLIENT_ID"));
		System.setProperty("GOOGLE_CLIENT_SECRET", System.getenv("GOOGLE_CLIENT_SECRET"));
		System.setProperty("GOOGLE_REDIRECT_URI", System.getenv("GOOGLE_REDIRECT_URI"));
		System.setProperty("GOOGLE_SCOPE", System.getenv("GOOGLE_SCOPE"));

		System.setProperty("PAYPAL_CLIENT_ID", System.getenv("PAYPAL_CLIENT_ID"));
		System.setProperty("PAYPAL_CLIENT_SECRET", System.getenv("PAYPAL_CLIENT_SECRET"));
		System.setProperty("PAYPAL_MODE", System.getenv("PAYPAL_MODE"));

		System.setProperty("vnp_TmnCode", System.getenv("vnp_TmnCode"));
		System.setProperty("vnp_HashSecret", System.getenv("vnp_HashSecret"));
		System.setProperty("vnp_Url", System.getenv("vnp_Url"));


		SpringApplication.run(CodearenaBackendApplication.class, args);
	}
}
