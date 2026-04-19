package com.maplewood.exception;

import com.maplewood.dto.ApiResponse;
import com.maplewood.config.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EnrollmentValidationException.class)
  public ResponseEntity<ApiResponse<Object>> handleValidation(EnrollmentValidationException exception)
  {
    return ResponseEntity.badRequest()
      .body(ApiResponse.failure(exception.getMessage(), 400));
  }
  @ExceptionHandler(InitializationException.class)
  public ResponseEntity<ApiResponse<Object>> handleInitializationException(InitializationException exception)
  {
    return ResponseEntity.badRequest()
      .body(ApiResponse.failure(exception.getMessage(), 400));
  }
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiResponse<Object>> handleBusiness(BusinessException exception)
  {
    return ResponseEntity.badRequest()
      .body(ApiResponse.failure(exception.getMessage(), 400));
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponse<Object>> handleNotFound(ResourceNotFoundException exception)
  {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(ApiResponse.failure(exception.getMessage(), 404));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> handleGeneral(Exception exception)
  {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(ApiResponse.failure(Constant.INTERNAL_SERVER_ERROR_MESSAGE, 500));
  }
}