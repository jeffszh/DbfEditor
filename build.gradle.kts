import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.4.32"
	id("com.github.johnrengelman.shadow") version ("4.0.4")
}

group = "cn.jeff.test"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}



tasks.withType<KotlinCompile>() {
	kotlinOptions.jvmTarget = "1.8"
}
