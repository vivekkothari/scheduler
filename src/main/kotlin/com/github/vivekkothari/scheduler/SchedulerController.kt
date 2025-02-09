package com.github.vivekkothari.scheduler

import com.github.vivekkothari.scheduler.model.ScheduleTask
import org.springframework.web.bind.annotation.*

/** The controller which exposes the REST apis to interact with the app. */
@RestController
data class SchedulerController(private val schedulerService: SchedulerService) {

  /** Say hello */
  @get:GetMapping("/") val sayHello: String = "Hello world"

  /** Schedule a task. */
  @PostMapping("/schedule")
  fun scheduleTask(task: ScheduleTask): String {
    return schedulerService.scheduleTask(task)
  }

  /** Cancel a task. */
  @DeleteMapping("/schedule/{id}")
  fun cancelTask(@PathVariable("id") id: String) {
    schedulerService.cancelTask(id)
  }
}
