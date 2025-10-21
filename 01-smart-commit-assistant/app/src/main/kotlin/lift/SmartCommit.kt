package lift

import com.aallam.openai.api.chat.*
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.*
import io.github.cdimascio.dotenv.dotenv
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class SmartCommit(private val model: String?) {

    suspend fun run() {
        val effectiveModel = model ?: "llama3"
        println("🔍 Gathering git diff...")

        val diff = getGitDiff()
        if (diff.isBlank()) {
            println("No staged changes found. Stage files and try again.")
            return
        }

        println("🧠 Generating commit message using $effectiveModel ...")

        when (effectiveModel.lowercase()) {
            "llama3" -> generateWithOllama(diff)
            "gpt-4o-mini" -> generateWithOpenAI(diff, effectiveModel)
            else -> {
                println("❌ Unknown model: $effectiveModel")
                println("Available models: llama3, gpt-4o-mini")
            }
        }
    }

    private fun getGitDiff(): String {
        val process = ProcessBuilder("git", "diff", "--cached")
            .redirectErrorStream(true)
            .start()

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        return reader.readText().trim()
    }

    private fun generateWithOllama(diff: String) {
        val json = JSONObject()
        json.put("model", "llama3")
        json.put(
            "prompt",
            "Generate a Conventional Commits style commit message (under 50 chars) for this diff:\n\n$diff"
        )

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url("http://localhost:11434/api/generate?stream=true")
            .post(requestBody)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Request failed: ${response.code}")

            val reader = BufferedReader(InputStreamReader(response.body!!.byteStream()))
            val finalOutput = StringBuilder()

            reader.forEachLine { line ->
                if (line.isNotBlank()) {
                    val jsonLine = JSONObject(line)
                    val token = jsonLine.optString("response")
                    finalOutput.append(token)
                }
            }

            println("\n💬 Suggested Commit Message:")
            println("👉 ${finalOutput.toString().trim()}")
        }
    }

    private suspend fun generateWithOpenAI(diff: String, model: String) {
        val dotenv = dotenv()
        val apiKey = dotenv["OPENAI_API_KEY"]
        if (apiKey.isNullOrBlank()) {
            println("⚠️ Missing OPENAI_API_KEY in .env file.")
            return
        }

        val openAI = OpenAI(apiKey)
        val response = openAI.chatCompletion(
            request = ChatCompletionRequest(
                model = ModelId(model),
                messages = listOf(
                    ChatMessage(
                        role = ChatRole.System,
                        content = "You are an expert software engineer who writes concise, meaningful git commit messages."
                    ),
                    ChatMessage(
                        role = ChatRole.User,
                        content = "Generate a Conventional Commits style commit message (under 50 chars) for this diff:\n\n$diff"
                    )
                ),
                maxTokens = 100
            )
        )

        val commitMessage = response.choices.first().message?.content?.trim()
        println("\n💬 Suggested Commit Message:")
        println("👉 $commitMessage")
    }
}
