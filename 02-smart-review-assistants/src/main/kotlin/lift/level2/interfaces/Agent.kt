package lift.level2.interfaces

interface Agent<I, O> {
    suspend fun execute(input: I): O
    val name: String
}
