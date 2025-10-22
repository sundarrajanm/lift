package lift.level2

import lift.level2.agents.*
import lift.level2.interfaces.LlmClient
import lift.level2.llm.Ollama
import lift.level2.model.CodeReviewInput
import lift.level2.utils.getGitDiff

suspend fun main() {
    val llmClient = object : LlmClient {
        override suspend fun complete(prompt: String): String {
            return Ollama().complete(prompt)
//            return OpenAI().complete(prompt)
        }
    }

    val reviewers = listOf(
        CleanReviewerAgent(llmClient),
        StyleReviewerAgent(llmClient),
        SecurityReviewerAgent(llmClient),
        PerformanceReviewerAgent(llmClient)
    )

    val orchestrator = ReviewOrchestrator(reviewers, LeadReviewerAgent())

    val review = orchestrator.review(CodeReviewInput(getGitDiff()))
    println(review)
}
