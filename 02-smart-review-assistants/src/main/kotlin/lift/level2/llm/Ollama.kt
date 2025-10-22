package lift.level2.llm

import lift.level2.interfaces.LlmClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class Ollama : LlmClient {
    override suspend fun complete(prompt: String): String {
        println("Ollama prompt: $prompt")
        val json = JSONObject()
        json.put("model", "llama3")
        json.put("prompt", prompt)

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

        val response = client.newCall(request).execute()
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
        return finalOutput.toString().trim()
    }
}
