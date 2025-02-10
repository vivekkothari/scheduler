package com.github.vivekkothari.scheduler.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Future
import java.time.Instant
import org.hibernate.validator.constraints.URL

/** Reqeust payload to schedule a task. */
data class ScheduleTaskRequest(
  @JsonProperty("executeAt")
  @field:Future(message = "executeAt must be in future")
  val executeAt: Instant,
  @JsonProperty("notificationUrl")
  @field:URL(message = "notificationUrl must be a valid URL", protocol = "https")
  val notificationUrl: String,
  @JsonProperty("httpMethod") val httpMethod: HttpMethod = HttpMethod.POST,
  @JsonProperty("payload") val payload: Map<*, *> = mapOf<Any, Any>(),
)

/** Enum for HTTP methods. */
enum class HttpMethod {
  GET,
  POST,
  PUT,
}

/** Response payload for a scheduled task. */
data class ScheduleTaskResponse(@JsonProperty("id") val id: String)
