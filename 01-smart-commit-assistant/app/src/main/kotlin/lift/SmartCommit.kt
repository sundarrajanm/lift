package lift

import com.aallam.openai.api.chat.*
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.*
import io.github.cdimascio.dotenv.dotenv
import java.io.BufferedReader
import java.io.InputStreamReader
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

suspend fun main() {
    val dotenv = dotenv()
    val apiKey = dotenv["OPENAI_API_KEY"]
    val openAI = OpenAI(apiKey)

    println("🔍 Gathering git diff...")
    val diff = getGitDiff()
    if (diff.isBlank()) {
        println("No staged changes found. Stage files and try again.")
        return
    }

    println("🧠 Generating commit message...")
//    val response = openAI.chatCompletion(
//        request = ChatCompletionRequest(
//            model = ModelId("gpt-4o-mini"),
//            messages = listOf(
//                ChatMessage(
//                    role = ChatRole.System,
//                    content = "You are an expert software engineer who writes concise, meaningful git commit messages."
//                ),
//                ChatMessage(
//                    role = ChatRole.User,
////                    content = "Generate a Conventional Commits commit message with less than 50 characters for this diff:\n\n$diff"
//                    content = "Generate a single-line git commit message for this diff:\n\n$diff"
//                )
//            ),
//            maxTokens = 100
//        )
//    )
//    val commitMessage = response.choices.first().message?.content?.trim()
//    println("\n💬 Suggested Commit Message:\n")
//    println("👉 $commitMessage")

    val json = JSONObject()
    json.put("model", "llama3")
//    json.put("prompt", "Generate a concise git commit message for this diff:\n$diff")
    json.put("prompt", "Generate a Conventional Commits commit message with less than 50 characters for this diff:\n\n$diff")

    val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
    val requestBody = json.toString().toRequestBody(mediaType)
    val request = Request.Builder()
        .url("http://localhost:11434/api/generate?stream=true") // enable streaming
        .post(requestBody)
        .build()

    try {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)  // time to connect
            .writeTimeout(30, TimeUnit.SECONDS)    // time to send the request
            .readTimeout(0, TimeUnit.SECONDS)    // 0 = infinite for streaming
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Request failed: ${response.code}")

            val reader = BufferedReader(InputStreamReader(response.body!!.byteStream()))
            val finalOutput = StringBuilder()

            reader.forEachLine { line ->
                if (line.isNotBlank()) {
                    val json = JSONObject(line)
                    val token = json.optString("response")
                    finalOutput.append(token)
                }
            }

            println("Generated commit message:\n${finalOutput.toString().trim()}")
        }
    } catch (e: IOException) {
        // Handle network errors
        e.printStackTrace()
    }
}

fun getGitDiff(): String {
    val process = ProcessBuilder("git", "diff", "--cached")
        .redirectErrorStream(true)
        .start()

    val reader = BufferedReader(InputStreamReader(process.inputStream))
    return reader.readText().trim()
}
