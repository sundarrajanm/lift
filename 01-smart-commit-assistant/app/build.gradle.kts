plugins {
    kotlin("jvm") version "2.0.0"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-okhttp:2.3.4")
    implementation("com.aallam.openai:openai-client:3.8.1") // OpenAI Kotlin SDK
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1") // for environment variables
    implementation("org.json:json:20240303")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

application {
    mainClass.set("lift.MainKt")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveBaseName.set("smart-commit")
    mergeServiceFiles()
}

tasks.register("installTool") {
    group = "distribution"
    description = "Installs the smart-commit CLI locally (no sudo required)"

    dependsOn("shadowJar")

    doLast {
        val jarPath = "${buildDir}/libs/smart-commit-all.jar"
        val installDir = File(System.getProperty("user.home"), ".local/bin")
        val scriptPath = File(installDir, "smart-commit")

        if (!installDir.exists()) {
            installDir.mkdirs()
        }

        println("🚀 Installing Smart Commit CLI to ${installDir.absolutePath} ...")

        // Copy JAR
        File(jarPath).copyTo(File(installDir, "smart-commit.jar"), overwrite = true)

        // Wrapper script
        val scriptContent = """
            #!/usr/bin/env bash
            java -jar "${installDir.absolutePath}/smart-commit.jar" "$@"
        """.trimIndent()

        val tempScript = File.createTempFile("smart-commit", ".tmp")
        tempScript.writeText(scriptContent)

        tempScript.copyTo(scriptPath, overwrite = true)
        scriptPath.setExecutable(true)

        println("✅ Installed successfully! Make sure ${installDir.absolutePath} is in your PATH.")
        println("Try running: smart-commit")
    }
}

tasks.register("installHook") {
    group = "tooling"
    description = "Installs the Smart Commit Assistant pre-commit hook."

    doLast {
        val hookScript = """
            #!/bin/bash
            echo "🧠 Smart Commit Assistant preview:"
            echo "-----------------------------------"
            smart-commit
            echo
            echo "-----------------------------------"
            echo "Press ENTER to continue with your commit..."
            
            read -r _ < /dev/tty

            exit 0
        """.trimIndent()

        val gitDir = file("${project.rootDir}/../.git/hooks")
        val hookFile = file("${gitDir}/pre-commit")

        if (!gitDir.exists()) {
            throw GradleException(".git directory not found. Please run this inside a Git repo.")
        }

        hookFile.writeText(hookScript)
        hookFile.setExecutable(true)

        println("✅ Installed Smart Commit Assistant pre-commit hook.")
    }
}
