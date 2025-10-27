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
data class CodeReviewInput(
    val diff: String,
    val fileName: String? = null,
    val context: String? = null
)

@Serializable
data class ConsolidatedReview(
    val summary: String,
    val nextSteps: List<String>,
    val allFindings: List<ReviewResult>
)

fun ConsolidatedReview.prettyPrint(): String = buildString {
    val RESET = "\u001B[0m"
    val BOLD = "\u001B[1m"
    val CYAN = "\u001B[36m"
    val GREEN = "\u001B[32m"
    val YELLOW = "\u001B[33m"
    val RED = "\u001B[31m"
    val MAGENTA = "\u001B[35m"
    val GRAY = "\u001B[90m"

    appendLine("${BOLD}${CYAN}🧩 Consolidated Code Review${RESET}")
    appendLine("=".repeat(80))
    appendLine()
    appendLine("${BOLD}${YELLOW}📋 Summary:${RESET}")
    appendLine(summary.trimIndent())
    appendLine()

    appendLine("${BOLD}${GREEN}🚀 Next Steps:${RESET}")
    if (nextSteps.isEmpty()) {
        appendLine("${GRAY}  No next steps provided.${RESET}")
    } else {
        nextSteps.forEachIndexed { idx, step ->
            appendLine("  ${idx + 1}. $step")
        }
    }
    appendLine()

    appendLine("${BOLD}${MAGENTA}🔍 Findings by Category:${RESET}")
    if (allFindings.isEmpty()) {
        appendLine("${GREEN}  ✅ No issues found — great job!${RESET}")
    } else {
        allFindings.forEach { result ->
            appendLine()
            appendLine("${BOLD}${CYAN}${result.category.uppercase()}${RESET}")
            appendLine("-".repeat(80))
            if (result.issues.isEmpty()) {
                appendLine("${GREEN}  ✅ No issues found in this category.${RESET}")
            } else {
                result.issues.forEachIndexed { idx, issue ->
                    val color = when (issue.severity.lowercase()) {
                        "high" -> RED
                        "medium" -> YELLOW
                        "low" -> GRAY
                        else -> RESET
                    }

                    appendLine("  ${color}${idx + 1}. ${issue.comment}${RESET}")
                    issue.line?.let { appendLine("     📍 Line: $it") }
                    appendLine()
                }
            }
        }
    }

    appendLine("=".repeat(80))
}
