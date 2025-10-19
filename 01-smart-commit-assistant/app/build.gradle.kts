plugins {
    kotlin("jvm") version "2.0.0"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.aallam.openai:openai-client:3.8.1") // OpenAI Kotlin SDK
    implementation("io.ktor:ktor-client-okhttp:2.3.4")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1") // for environment variables
    implementation("org.json:json:20240303")
}

application {
    mainClass.set("lift.SmartCommitKt")
}
