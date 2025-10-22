package lift.level2.agents

import lift.level2.interfaces.Agent
import lift.level2.model.ConsolidatedReview
import lift.level2.model.ReviewResult

class LeadReviewerAgent : Agent<List<ReviewResult>, ConsolidatedReview> {
    override val name = "LeadReviewer"

    override suspend fun execute(input: List<ReviewResult>): ConsolidatedReview {
        val totalIssues = input.sumOf { it.issues.size }
        val summary = "Found $totalIssues total issues across ${input.size} reviewers."

        val nextSteps = input.flatMap { review ->
            review.issues.map { "${review.category}: ${it.comment}" }
        }

        return ConsolidatedReview(summary, nextSteps.take(5), input)
    }
}
