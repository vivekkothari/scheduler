package com.github.vivekkothari.scheduler.configuration

import java.time.Duration
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "scheduler")
/** Application configuration. */
data class SchedulerConfig(
  val threadCount: Int,
  val pollingDuration: Duration,
  val lowerLimitFractionOfThreads: Double,
  val upperLimitFractionOfThreads: Double,
  val httpTaskName: String,
  val maxRetries: Int = 3,
  val retryBackoffMultiplier: Long = 5,
)
