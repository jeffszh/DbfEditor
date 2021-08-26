import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.4.32"
	id("com.github.johnrengelman.shadow") version ("4.0.4")
}

group = "cn.jeff.app"
version = "1.0-SNAPSHOT"

repositories {
	mavenLocal()
	mavenCentral()
	maven("https://maven.aliyun.com/repository/public")
	maven("https://maven.aliyun.com/nexus/content/groups/public/")
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")

	implementation("no.tornado:tornadofx:1.7.17")

	// https://mvnrepository.com/artifact/com.google.code.gson/gson
	implementation(
		"com.google.code.gson",
		name = "gson",
		version = "2.8.6"
	)

	// https://mvnrepository.com/artifact/com.github.albfernandez/javadbf
	@Suppress("SpellCheckingInspection")
	implementation("com.github.albfernandez", "javadbf", "1.13.1")

	// 下面这句已经没用了，用JDBC更麻烦，改回原先的方法。但下面这句作为添加本地jar的例子，留着。
	implementation(fileTree(mapOf("dir" to "libs/", "include" to listOf("*.jar"))))
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "1.8"
}

tasks.shadowJar {
	manifest {
		attributes["Main-Class"] = "cn.jeff.app.DbfEditor"
	}
}
