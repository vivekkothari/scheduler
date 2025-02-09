package com.github.vivekkothari.scheduler.model

import java.time.Instant

/** The request payload to schedule a task. */
data class ScheduleTask(
  val executeAt: Instant,
  val notificationUrl: String,
  val payload: Map<*, *>,
)
