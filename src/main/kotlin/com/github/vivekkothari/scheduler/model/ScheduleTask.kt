package com.github.vivekkothari.scheduler.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.vivekkothari.scheduler.dto.HttpMethod

/** The request payload to schedule a task. */
data class ScheduleTask
@JsonCreator
constructor(
  @JsonProperty("notificationUrl") val notificationUrl: String,
  @JsonProperty("httpMethod") val httpMethod: HttpMethod = HttpMethod.POST,
  @JsonProperty("payload") val payload: Map<*, *> = mapOf<Any, Any>(),
)
