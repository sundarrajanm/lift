package lift.level2.utils

import java.io.BufferedReader
import java.io.InputStreamReader

fun getGitDiff(): String {
    val process = ProcessBuilder("git", "diff", "--cached")
        .redirectErrorStream(true)
        .start()

    val reader = BufferedReader(InputStreamReader(process.inputStream))
    return reader.readText().trim()
}
