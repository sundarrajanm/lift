package lift.level2.model

import kotlinx.serialization.Serializable

@Serializable
data class ReviewIssue(
    val line: Int? = null,
    val comment: String,
    val severity: String = "medium"
)

@Serializable
data class ReviewResult(
    val category: String,
    val issues: List<ReviewIssue>
)


@Serializable
data class ConsolidatedReview(
    val summary: String,
    val nextSteps: List<String>,
    val allFindings: List<ReviewResult>
)


@Serializable
data class CodeReviewInput(
    val diff: String,
    val fileName: String? = null,
    val context: String? = null
)
