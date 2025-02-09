package com.github.vivekkothari.scheduler

import com.github.vivekkothari.scheduler.dto.ScheduleTaskRequest
import com.github.vivekkothari.scheduler.dto.ScheduleTaskResponse
import org.springframework.web.bind.annotation.*

/** The controller which exposes the REST apis to interact with the app. */
@RestController
@RequestMapping("/api")
data class SchedulerController(private val schedulerService: SchedulerService) {

  /** Say hello */
  @get:GetMapping("/") val sayHello: String = "Hello world"

  /** Schedule a task. */
  @PutMapping("/schedule")
  fun scheduleTask(@RequestBody task: ScheduleTaskRequest): ScheduleTaskResponse {
    return schedulerService.scheduleTask(task)
  }

  /** Cancel a task. */
  @DeleteMapping("/schedule/{id}")
  fun cancelTask(@PathVariable("id") id: String) {
    schedulerService.cancelTask(id)
  }
}
