package lift.level2.agents

import lift.level2.interfaces.LlmClient

class CleanReviewerAgent(modelClient: LlmClient)
    : LlmReviewerAgent("CleanReviewer", modelClient) {
    override fun systemPrompt() = """
        You are a code cleanliness reviewer.
        Focus on readability, naming conventions, and maintainability.
        Identify unclear variable names, deeply nested logic, or magic numbers.
    """.trimIndent()
}
