package lift.smartcommit.demo

/**
 * This file exists purely as a baseline for demo patches.
 *
 * During the LIFT Level 1 session, we'll apply patches like:
 *   - fix: error handling
 *   - feat: dry-run mode
 *   - refactor: extract logic
 *   - docs: add usage notes
 *   - perf: cache optimization
 *
 * Each patch modifies or extends this baseline to demonstrate how
 * the Smart Commit Assistant summarizes meaningful diffs.
 */
object SmartCommitDemoBaseline {

    fun greet(): String {
        return "Hello from Smart Commit Assistant!"
    }

    fun mainFeature(): String {
        return "This is the baseline feature."
    }

    fun buggyFunction(): Int {
        val input: String? = null
        return input!!.length
    }
}
