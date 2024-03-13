import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.gradle.internal.impldep.com.fasterxml.jackson.core.JsonPointer.compile

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
//  implementation("io.vertx:vertx-rx-java3")
//  implementation("io.vertx:vertx-mongo-client")
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
//  compile("io.ebean:ebean:12.7.2")
  // query bean generation
//  annotationProcessor("io.ebean:querybean-generator:11.27.1")
//  testCompileOnly("io.ebean:ebean-test:12.7.2")
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
  args = listOf("run", mainVerticleName, "-conf /Users/macbook/workspace/pratice/vertx-web-app/src/main/resources/config.json","--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
