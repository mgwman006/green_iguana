package com.maplewood.exception;

/**
 * Base exception for all initialization-related failures
 * during application startup (e.g., section generation).
 */
public class InitializationException extends RuntimeException
{

  public InitializationException(String message)
  {
    super(message);
  }

  public InitializationException(String message, Throwable cause)
  {
    super(message, cause);
  }
}