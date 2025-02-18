package com.github.vivekkothari.scheduler

import com.github.kagkarlsson.scheduler.Scheduler
import com.github.kagkarlsson.scheduler.task.TaskInstanceId
import com.github.vivekkothari.scheduler.configuration.SchedulerConfig
import com.github.vivekkothari.scheduler.dto.HttpMethod
import com.github.vivekkothari.scheduler.dto.ScheduleTaskRequest
import com.github.vivekkothari.scheduler.dto.ScheduleTaskResponse
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName
import java.time.Instant

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = [SchedulerApplication::class],
)
@EnableAutoConfiguration
class SchedulerApplicationTest {

  private val logger = LoggerFactory.getLogger(this.javaClass.name)

  companion object {
    @Container
    var postgres: PostgreSQLContainer<*> =
      PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"))
        .withInitScripts("sql/scheduler.sql")

    @BeforeAll
    @JvmStatic
    fun beforeAll() {
      postgres.start()
    }

    @AfterAll
    @JvmStatic
    fun afterAll() {
      postgres.stop()
    }

    @JvmStatic
    @DynamicPropertySource
    fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
      registry.add("dbUrl", postgres::getJdbcUrl)
      registry.add("dbUser", postgres::getUsername)
      registry.add("dbPassword", postgres::getPassword)
    }
  }

  @Autowired private lateinit var webTestClient: WebTestClient
  @Autowired private lateinit var scheduler: Scheduler
  @Autowired private lateinit var config: SchedulerConfig

  @Test
  fun contextLoads() {
    logger.info("Testing the context load")
    val responseSpec =
      webTestClient
        .put()
        .uri("/api/schedule")
        .bodyValue(
          ScheduleTaskRequest(
            Instant.now().plusSeconds(3600),
            "https://localhost:8080/notify",
            HttpMethod.POST,
            mapOf("key" to "value"),
          )
        )
        .exchange()
    responseSpec.expectStatus().isOk
    val returnResult = responseSpec.expectBody(ScheduleTaskResponse::class.java).returnResult()
    returnResult.responseBody?.id?.let {
      Assertions.assertTrue(it.isNotEmpty())
      Assertions.assertTrue(
        scheduler.getScheduledExecution(TaskInstanceId.of(config.httpTaskName, it)).isPresent
      )

      webTestClient.delete().uri("/api/schedule/$it").exchange().expectStatus().isOk
      Assertions.assertTrue(
        scheduler.getScheduledExecution(TaskInstanceId.of(config.httpTaskName, it)).isEmpty
      )
    }
  }
}
