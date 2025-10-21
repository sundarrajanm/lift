package lift
import java.io.File
import org.json.JSONObject

data class Prompts(
    val systemPrompt: String,
    val userPrompt: String
) {
    companion object {
        fun loadFromFile(path: String): Prompts? {
            println("Loading $path")
            val file = File(path)
            if (!file.exists()) return null
            return try {
                println("Loading prompt from $file")
                val json = JSONObject(file.readText())
                val prompts = Prompts(
                    systemPrompt = json.optString("systemPrompt"),
                    userPrompt = json.optString("userPrompt")
                )
                println("Using prompts from: ${path}, ${prompts}")
                prompts
            } catch (e: Exception) {
                println("⚠️ Failed to parse prompts.json, using defaults.")
                null
            }
        }
    }
}
