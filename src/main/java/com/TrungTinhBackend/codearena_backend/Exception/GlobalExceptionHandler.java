package com.TrungTinhBackend.codearena_backend.Exception;

import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponse> handleRuntimeException(RuntimeException ex) {
        APIResponse response = new APIResponse();
        response.setStatusCode(400L);
        response.setMessage("Error: " + ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setData(null); // Không có dữ liệu trả về
        response.setToken(null); // Không cần token ở đây

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Xử lý lỗi chung (Exception)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleGeneralException(Exception ex) {
        APIResponse response = new APIResponse();
        response.setStatusCode(500L);
        response.setMessage("System error: " + ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setData(null);
        response.setToken(null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
