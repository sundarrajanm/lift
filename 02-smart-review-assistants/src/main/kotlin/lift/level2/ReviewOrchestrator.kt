package lift.level2

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import lift.level2.agents.LeadReviewerAgent
import lift.level2.interfaces.Agent
import lift.level2.model.CodeReviewInput
import lift.level2.model.ConsolidatedReview
import lift.level2.model.ReviewResult

class ReviewOrchestrator(
    private val reviewers: List<Agent<CodeReviewInput, ReviewResult>>,
    private val leadReviewer: LeadReviewerAgent
) {
    suspend fun review(input: CodeReviewInput): ConsolidatedReview = coroutineScope {
        val results = reviewers.map { reviewer -> async { reviewer.execute(input) } }.awaitAll()
        leadReviewer.execute(results)
    }
}
