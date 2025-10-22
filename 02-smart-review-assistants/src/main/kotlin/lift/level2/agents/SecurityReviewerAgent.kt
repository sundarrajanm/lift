package lift.level2.agents

import lift.level2.interfaces.LlmClient

class SecurityReviewerAgent(modelClient: LlmClient)
    : LlmReviewerAgent("CleanReviewer", modelClient) {
    override fun systemPrompt() = """
        You are a security reviewer.
        Identify insecure patterns like hard-coded credentials, unvalidated input, or weak cryptography.
    """.trimIndent()
}
