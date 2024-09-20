import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("io.spring.dependency-management") version "1.0.1.RELEASE"
  id("io.ebean") version "12.7.2"
}

group = "com.moonlight"
version = "1.0.0-SNAPSHOT"
apply(plugin = "io.ebean")

repositories {
  mavenCentral()
}

val vertxVersion = "3.8.1"
val junitJupiterVersion = "5.9.1"
val jacksonVersion = "2.9.9"

val mainVerticleName = "com.moonlight.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencyManagement{
  imports{
    mavenBom("org.apache.logging.log4j:log4j-bom:2.14.0")
  }
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web-client")
  implementation("io.vertx:vertx-web")
//  implementation("io.vertx:vertx-mysql-client")
  implementation("io.vertx:vertx-rx-java:3.8.1")
  implementation("io.vertx:vertx-mongo-client:3.8.1")

  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
  // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
  implementation("org.slf4j:slf4j-api:1.7.25")
  implementation("org.apache.logging.log4j:log4j-slf4j-impl")
  compileOnly("org.projectlombok:lombok:1.18.20")
  annotationProcessor("org.projectlombok:lombok:1.18.20")
  implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
  implementation("com.mysql:mysql-connector-j:8.3.0")
  implementation("io.ebean:ebean:12.7.2")
  implementation("com.google.code.gson:gson:2.8.8")
  implementation("com.squareup.retrofit2:retrofit:2.7.1")
  implementation("com.squareup.retrofit2:converter-gson:2.7.1")
  implementation("com.squareup.okhttp3:logging-interceptor:3.7.0")
  // https://mvnrepository.com/artifact/com.itextpdf/itextpdf
  implementation("com.itextpdf:itextpdf:5.5.13")
// https://mvnrepository.com/artifact/org.xhtmlrenderer/flying-saucer-pdf
  implementation("org.xhtmlrenderer:flying-saucer-pdf:9.1.8")

  implementation("com.itextpdf.tool:xmlworker:5.5.13")
  implementation("org.jsoup:jsoup:1.13.1")
//  compile("io.ebean:ebean:12.7.2")
  // query bean generation
//  annotationProcessor("io.ebean:querybean-generator:11.27.1")
//  testCompileOnly("io.ebean:ebean-test:12.7.2")
  implementation("com.auth0:java-jwt:3.4.0")
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName,
    "-conf /Users/moonlight/workspace/practice/vertx-web-app/src/main/resources/config.json",
    "--redeploy=$watchForChange",
    "--launcher-class=$launcherClassName",
    "--on-redeploy=$doOnChange")
}
