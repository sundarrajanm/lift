package lift

import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    val model = args.firstOrNull()
    SmartCommit(model).run()
}