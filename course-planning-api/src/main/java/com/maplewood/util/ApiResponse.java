package com.maplewood.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiResponse<T>
{

  private boolean success;
  private String message;
  private T data;
  private int statusCode;

  // Static factory methods
  public static <T> ApiResponse<T> success(T data, int statusCode)
  {
    return new ApiResponse<>(true, "Request successful", data, statusCode);
  }

  public static <T> ApiResponse<T> success(String message, T data, int statusCode)
  {
    return new ApiResponse<>(true, message, data, statusCode);
  }

  public static <T> ApiResponse<T> failure(String message, int statusCode)
  {
    return new ApiResponse<>(false, message, null, statusCode);
  }

  // Getters and setters
  public boolean isSuccess()
  {
    return success;
  }

  public String getMessage()
  {
    return message;
  }

  public T getData()
  {
    return data;
  }

  public int getStatusCode()
  {
    return statusCode;
  }
}

