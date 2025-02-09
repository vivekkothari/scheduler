package com.github.vivekkothari.scheduler

import com.github.vivekkothari.scheduler.configuration.SchedulerConfig
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(SchedulerConfig::class)
@EnableAutoConfiguration
/** Application entry point. */
class SchedulerApplication

/** Entry point method */
fun main() {
  runApplication<SchedulerApplication>()
}
