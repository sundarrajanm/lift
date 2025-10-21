package lift
import java.io.File
import org.json.JSONObject

data class Prompts(
    val systemPrompt: String,
    val userPrompt: String
) {
    companion object {
        fun loadFromFile(path: String): Prompts? {
            val file = File(path)
            if (!file.exists()) return null
            return try {
                val json = JSONObject(file.readText())
                Prompts(
                    systemPrompt = json.optString("systemPrompt"),
                    userPrompt = json.optString("userPrompt")
                )
            } catch (e: Exception) {
                println("⚠️ Failed to parse prompts.json, using defaults.")
                null
            }
        }
    }
}
