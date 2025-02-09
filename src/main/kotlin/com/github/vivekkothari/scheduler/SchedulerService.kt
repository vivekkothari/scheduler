package com.github.vivekkothari.scheduler

import com.github.kagkarlsson.scheduler.Scheduler
import com.github.kagkarlsson.scheduler.task.FailureHandler
import com.github.kagkarlsson.scheduler.task.TaskInstance
import com.github.kagkarlsson.scheduler.task.TaskInstanceId
import com.github.kagkarlsson.scheduler.task.helper.OneTimeTask
import com.github.kagkarlsson.scheduler.task.helper.Tasks.oneTime
import com.github.vivekkothari.scheduler.configuration.SchedulerConfig
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
  private val schedulerConfig: SchedulerConfig,
) {

  private val logger = LoggerFactory.getLogger(this.javaClass.name)

  /** Schedules as task */
  fun scheduleTask(task: ScheduleTask): String {
    val id = UUID.randomUUID().toString()
    scheduler.schedule(TaskInstance(schedulerConfig.httpTaskName, id, task), task.executeAt)
    return id
  }

  /** Cancels a scheduled task. */
  fun cancelTask(id: String) {
    scheduler.cancel(TaskInstanceId.of(schedulerConfig.httpTaskName, id))
  }

  /** Http task which will call http url with the payload. */
  @Bean
  fun httpTask(): OneTimeTask<ScheduleTask> {
    return oneTime(schedulerConfig.httpTaskName, ScheduleTask::class.java)
      .onFailure(
        FailureHandler.MaxRetriesFailureHandler(3) { executionComplete, executionOperations ->
          logger.warn(
            "Execution failed {} re-trying {}",
            executionComplete.execution.consecutiveFailures,
            executionComplete.execution.taskInstance.taskAndInstance,
            executionComplete.cause.orElse(null),
          )
          executionOperations.reschedule(
            executionComplete,
            Instant.now().plusSeconds(executionComplete.execution.consecutiveFailures * 5L),
          )
        }
      )
      .execute { inst, _ -> run(inst) }
  }

  /** Runs a task */
  fun run(inst: TaskInstance<ScheduleTask>) {
    logger.info("Running ${inst.data}")
  }
  //
  //  /** Starts the scheduler */
  //  @PostConstruct
  //  fun startScheduler() {
  //    logger.info("✅ Scheduler started on application startup")
  //    scheduler.start()
  //  }
  //
  //  /** Hook to stop the scheduler when server stops. */
  //  @PreDestroy
  //  fun stopService() {
  //    logger.info("❌ Scheduler shutting down...")
  //    scheduler.stop()
  //  }
}
