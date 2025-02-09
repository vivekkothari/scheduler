plugins {
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
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
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-jdbc")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

  implementation("com.zaxxer:HikariCP:6.2.1")
  implementation("com.github.kagkarlsson:db-scheduler:15.1.1")

  runtimeOnly("org.postgresql:postgresql")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
