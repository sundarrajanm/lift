package lift

import kotlinx.coroutines.runBlocking
import java.io.File

fun main(args: Array<String>) = runBlocking {
    val configDir = File("${System.getProperty("user.home")}/.lift")
    if (!configDir.exists()) configDir.mkdirs()

    val model = args.firstOrNull()
    SmartCommit(model).run()
}