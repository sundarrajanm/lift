package lift.level2.interfaces

interface LlmClient {
    suspend fun complete(prompt: String): String
}
