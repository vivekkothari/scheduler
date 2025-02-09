package com.github.vivekkothari.scheduler.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/** The request payload to schedule a task. */
data class ScheduleTask
@JsonCreator
constructor(
  @JsonProperty("notificationUrl") val notificationUrl: String,
  @JsonProperty("payload") val payload: Map<*, *> = mapOf<Any, Any>(),
)
