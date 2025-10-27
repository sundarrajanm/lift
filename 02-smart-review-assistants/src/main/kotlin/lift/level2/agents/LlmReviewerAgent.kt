package lift.level2.agents

import kotlinx.serialization.json.Json
import lift.level2.model.CodeReviewInput
import lift.level2.model.ReviewResult
import lift.level2.interfaces.Agent
import lift.level2.interfaces.LlmClient

abstract class LlmReviewerAgent(
    override val name: String,
    private val modelClient: LlmClient
) : Agent<CodeReviewInput, ReviewResult> {

    abstract fun systemPrompt(): String

    override suspend fun execute(input: CodeReviewInput): ReviewResult {
        val prompt = """
            You are a JSON-only assistant.
            Always respond with valid JSON matching this schema:
            {
            "category": "...",
            "issues": [ { "line": 0, "comment": "...", "severity": "low|medium|high" } ]
            }
            Do not include explanations or markdown. Output only JSON.

            ${systemPrompt()}
            
            Code Diff:
            ${input.diff}            
            """.trimIndent()

        val rawOutput = modelClient.complete(prompt)
        return Json.decodeFromString(rawOutput)
    }
}
