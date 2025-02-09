package com.github.vivekkothari.scheduler

import com.github.kagkarlsson.scheduler.Scheduler
import com.github.kagkarlsson.scheduler.task.FailureHandler
import com.github.kagkarlsson.scheduler.task.TaskInstance
import com.github.kagkarlsson.scheduler.task.TaskInstanceId
import com.github.kagkarlsson.scheduler.task.helper.OneTimeTask
import com.github.kagkarlsson.scheduler.task.helper.Tasks.oneTime
import com.github.vivekkothari.scheduler.configuration.SchedulerConfig
import com.github.vivekkothari.scheduler.dto.ScheduleTaskRequest
import com.github.vivekkothari.scheduler.dto.ScheduleTaskResponse
import com.github.vivekkothari.scheduler.model.ScheduleTask
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

/** Service which schedules and cancels tasks. */
@Service
data class SchedulerService(
  @Lazy private val scheduler: Scheduler,
  private val config: SchedulerConfig,
) {

  private val logger = LoggerFactory.getLogger(this.javaClass.name)

  /** Schedules as task */
  fun scheduleTask(task: ScheduleTaskRequest): ScheduleTaskResponse {
    val id = UUID.randomUUID().toString()
    scheduler.schedule(
      TaskInstance(config.httpTaskName, id, ScheduleTask(task.notificationUrl, task.payload)),
      task.executeAt,
    )
    return ScheduleTaskResponse(id)
  }

  /** Cancels a scheduled task. */
  fun cancelTask(id: String) {
    // Handle the case where the task is not found and throw an exception
    scheduler.cancel(TaskInstanceId.of(config.httpTaskName, id))
  }

  /** Http task which will call http url with the payload. */
  @Bean
  fun httpTask(): OneTimeTask<ScheduleTask> {
    return oneTime(config.httpTaskName, ScheduleTask::class.java)
      .onFailure(
        FailureHandler.MaxRetriesFailureHandler(config.maxRetries) { executionComplete,
                                                                     executionOperations ->
          logger.warn(
            "Execution failed {} re-trying {}",
            executionComplete.execution.consecutiveFailures,
            executionComplete.execution.taskInstance.taskAndInstance,
            executionComplete.cause.orElse(null),
          )
          executionOperations.reschedule(
            executionComplete,
            Instant.now()
              .plusSeconds(
                executionComplete.execution.consecutiveFailures * config.retryBackoffMultiplier
              ),
          )
        }
      )
      .execute { inst, _ -> run(inst) }
  }

  /** Runs a task */
  fun run(inst: TaskInstance<ScheduleTask>) {
    logger.info("Running ${inst.data}")
  }
}
