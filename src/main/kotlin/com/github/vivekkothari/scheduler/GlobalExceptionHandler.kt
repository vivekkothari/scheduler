package com.github.vivekkothari.scheduler

import com.github.kagkarlsson.scheduler.exceptions.TaskInstanceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException

/** Global exception handler. */
@ControllerAdvice
class GlobalExceptionHandler {

  /** Handle TaskInstanceNotFoundException. */
  @ExceptionHandler(TaskInstanceNotFoundException::class)
  fun handleTaskNotFoundException(): ResponseEntity<String> {
    return ResponseEntity("Task not found", HttpStatus.NOT_FOUND)
  }

  /** Handle MethodArgumentNotValidException. */
  @ExceptionHandler(WebExchangeBindException::class)
  fun handleValidationExceptions(ex: WebExchangeBindException): ResponseEntity<Any> {
    val errors: MutableMap<String, String?> = HashMap()
    ex.bindingResult.allErrors.forEach { error: ObjectError ->
      val fieldName = (error as FieldError).field
      val errorMessage = error.getDefaultMessage()
      errors[fieldName] = errorMessage
    }
    return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
  }
}
