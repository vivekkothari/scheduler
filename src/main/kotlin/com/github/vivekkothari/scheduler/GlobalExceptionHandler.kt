package com.github.vivekkothari.scheduler

import com.github.kagkarlsson.scheduler.exceptions.TaskInstanceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/** Global exception handler. */
@ControllerAdvice
class GlobalExceptionHandler {

    /** Handle TaskInstanceNotFoundException. */
    @ExceptionHandler(TaskInstanceNotFoundException::class)
    fun handleTaskNotFoundException(): ResponseEntity<String> {
        return ResponseEntity("Task not found", HttpStatus.NOT_FOUND)
    }
}
