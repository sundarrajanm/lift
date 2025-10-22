package lift.level2.llm

import lift.level2.interfaces.LlmClient

class OpenAI : LlmClient {
    override suspend fun complete(prompt: String): String {
        TODO("Not yet implemented")
    }
}