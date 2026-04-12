package com.maplewood.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Result<T>
{
  private boolean success;
  private String message;
  private T data;

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

  public static <T> Result<T> success(String message, T data)
  {
    return new Result<>(true, message, data);
  }

  public static <T> Result<T> failure(String message)
  {
    return new Result<>(false, message, null);
  }

}
