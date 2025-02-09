package com.github.vivekkothari.scheduler.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

/** Reqeust payload to schedule a task. */
data class ScheduleTaskRequest(
  @JsonProperty("executeAt") val executeAt: Instant,
  @JsonProperty("notificationUrl") val notificationUrl: String,
  @JsonProperty("payload") val payload: Map<*, *> = mapOf<Any, Any>(),
)

/** Response payload for a scheduled task. */
data class ScheduleTaskResponse(@JsonProperty("id") val id: String)
