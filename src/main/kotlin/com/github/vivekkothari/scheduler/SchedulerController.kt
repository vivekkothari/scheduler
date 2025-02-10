package com.github.vivekkothari.scheduler

import com.github.vivekkothari.scheduler.dto.ScheduleTaskRequest
import com.github.vivekkothari.scheduler.dto.ScheduleTaskResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

/** The controller which exposes the REST apis to interact with the app. */
@RestController
@RequestMapping("/api")
@Tag(name = "Scheduler", description = "Scheduler API")
data class SchedulerController(private val schedulerService: SchedulerService) {

  /** Schedule a task. */
  @PutMapping("/schedule", consumes = ["application/json"], produces = ["application/json"])
  @Operation(summary = "Schedule a task")
  fun scheduleTask(@Valid @RequestBody task: ScheduleTaskRequest): ScheduleTaskResponse {
    return schedulerService.scheduleTask(task)
  }

  /** Cancel a task. */
  @DeleteMapping("/schedule/{id}")
  @Operation(summary = "Cancel a task")
  fun cancelTask(@PathVariable("id") id: String) {
    schedulerService.cancelTask(id)
  }
}
