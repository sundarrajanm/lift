package lift.level2.llm

import com.aallam.openai.api.chat.*
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.*
import io.github.cdimascio.dotenv.dotenv
import lift.level2.interfaces.LlmClient

class OpenAI : LlmClient {
    override suspend fun complete(prompt: String): String {
        println("OpenAI prompts: $prompt")
        val dotenv = dotenv()
        val apiKey = dotenv["OPENAI_API_KEY"]
        if (apiKey.isNullOrBlank()) {
            println("⚠️ Missing OPENAI_API_KEY in .env file.")
            return ""
        }

        val openAI = OpenAI(apiKey)
        val response = openAI.chatCompletion(
            request = ChatCompletionRequest(
                model = ModelId("gpt-4o-mini"),
                messages = listOf(
                    ChatMessage(
                        role = ChatRole.System,
                        content = "TODO"
                    ),
                    ChatMessage(
                        role = ChatRole.User,
                        content = "TODO"
                    )
                ),
                maxTokens = 100
            )
        )

        val commitMessage = response.choices.first().message?.content?.trim()
        println("\n💬 Suggested Commit Message:")
        return "👉 $commitMessage"
    }
}
