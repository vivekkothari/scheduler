package com.github.vivekkothari.scheduler.configuration

import com.github.kagkarlsson.scheduler.Scheduler
import com.github.kagkarlsson.scheduler.serializer.JacksonSerializer
import com.github.kagkarlsson.scheduler.task.helper.OneTimeTask
import com.github.vivekkothari.scheduler.model.ScheduleTask
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import javax.sql.DataSource
import org.slf4j.LoggerFactory
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient

/** Class responsible for wiring all the dependency. */
@Configuration
data class SchedulerConfiguration(val config: SchedulerConfig) {

  /** Wires up the [Scheduler]. */
  @Bean
  fun scheduler(datasource: DataSource, httpTask: OneTimeTask<ScheduleTask>): Scheduler {
    return Scheduler.create(datasource, httpTask)
      .enableImmediateExecution()
      .threads(config.threadCount)
      .pollingInterval(config.pollingDuration)
      .pollUsingLockAndFetch(config.lowerLimitFractionOfThreads, config.upperLimitFractionOfThreads)
      .serializer(JacksonSerializer())
      .build()
  }

  /** Wires up the [InMemoryHttpExchangeRepository] which is used to store the http exchanges. */
  @Bean
  fun httpExchangeRepository(): HttpExchangeRepository {
    return InMemoryHttpExchangeRepository()
  }

  /** Wires up the [RestTemplate]. */
  @Bean
  fun webClient(): WebClient {
    return WebClient.builder().build()
  }

  /** Life cycle handler. */
  @Service
  data class SchedulerLifeCycle(private val scheduler: Scheduler) {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    /** Starts the scheduler */
    @PostConstruct
    fun startScheduler() {
      logger.info("✅ Scheduler started on application startup")
      scheduler.start()
    }

    /** Hook to stop the scheduler when server stops. */
    @PreDestroy
    fun stopService() {
      logger.info("❌ Scheduler shutting down...")
      scheduler.stop()
    }
  }
}
