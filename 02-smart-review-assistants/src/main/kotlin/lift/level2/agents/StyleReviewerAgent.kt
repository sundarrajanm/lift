package lift.level2.agents

import lift.level2.interfaces.LlmClient

class StyleReviewerAgent(modelClient: LlmClient)
    : LlmReviewerAgent("CleanReviewer", modelClient) {
    override fun systemPrompt() = """
        You are a style reviewer.
        Check for idiomatic Kotlin usage, consistent formatting, and adherence to code style.
    """.trimIndent()
}
