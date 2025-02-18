plugins {
  kotlin("jvm") version "2.0.10"
  kotlin("plugin.spring") version "2.0.10"
  id("org.springframework.boot") version "3.4.2"
  id("io.spring.dependency-management") version "1.1.7"
  id("com.ncorti.ktfmt.gradle") version "0.21.0"
  id("io.gitlab.arturbosch.detekt") version "1.23.7"
}

group = "com.github.vivekkothari"

version = "0.0.1-SNAPSHOT"

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

repositories { mavenCentral() }

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux") {
    because("To expose the non-blocking REST apis.")
  }
  implementation("org.springframework.boot:spring-boot-starter-jdbc") {
    because("To interact with the database.")
  }
  implementation("org.springframework.boot:spring-boot-starter-actuator") {
    because("To expose the health and other actuator endpoints.")
  }
  implementation("org.springframework.boot:spring-boot-starter-validation") {
    because("To validate the request payload.")
  }
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

  developmentOnly("org.springframework.boot:spring-boot-docker-compose") {
    because("To run the app in docker-compose for development.")
  }

  implementation("com.zaxxer:HikariCP:6.2.1") { because("To use the HikariCP connection pool.") }
  implementation("com.github.kagkarlsson:db-scheduler:15.1.1") { because("To schedule the tasks.") }
  implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.8.4") {
    because("To expose the swagger ui.")
  }

  runtimeOnly("org.postgresql:postgresql") { because("Postgres database driver.") }

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  testImplementation("org.testcontainers:junit-jupiter:1.19.8")
  testImplementation("org.testcontainers:postgresql:1.19.8")
}

kotlin { compilerOptions { freeCompilerArgs.addAll("-Xjsr305=strict") } }

ktfmt {
  // Google style - 2 space indentation & automatically adds/removes trailing commas
  // Also using google style ensures that switching between java and kotlin code feels more natural.
  googleStyle()
}

// detekt configuration

detekt {
  autoCorrect = true
  config.setFrom(file("${rootProject.projectDir}/config/detekt/detekt.yml"))
  buildUponDefaultConfig = true
}

tasks.withType<Test> { useJUnitPlatform() }
