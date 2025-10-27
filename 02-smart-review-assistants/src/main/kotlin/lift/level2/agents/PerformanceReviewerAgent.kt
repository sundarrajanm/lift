package lift.level2.agents

import lift.level2.interfaces.LlmClient

class PerformanceReviewerAgent(modelClient: LlmClient)
    : LlmReviewerAgent("PerformanceReviewer", modelClient) {
    override fun systemPrompt() = """
        You are a performance reviewer.
        Look for inefficiencies such as redundant loops, heavy allocations, or unnecessary computations.
    """.trimIndent()
}
