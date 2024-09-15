package org.example.interactions.commands

import dev.kord.core.event.message.MessageCreateEvent
import org.reflections.Reflections
import kotlin.reflect.full.createInstance

interface Command {
    val name: String
    val description: String
    val permissions: String // Example permission structure. Modify as needed.
    val params: String

    suspend fun execute(event: MessageCreateEvent, commands: Map<String, Command> = mapOf())
}

class CommandHandler {

    private val commands: Map<String, Command> = loadCommands()

    private fun loadCommands(): Map<String, Command> {
        val reflections = Reflections("org.example.interactions.commands")
        // Find all classes that implement Command interface
        val commandClasses = reflections.getSubTypesOf(Command::class.java)
        return commandClasses.mapNotNull { clazz ->
            try {
                // Create an instance of each command
                val instance = clazz.kotlin.createInstance()
                // Map command name to instance
                instance.name to instance
            } catch (e: Exception) {
                println("Failed to create instance of ${clazz.simpleName}: ${e.message}")
                null
            }
        }.toMap()
    }

    suspend fun handle(event: MessageCreateEvent) {
        val content = event.message.content
        if (content.startsWith("!")) {
            val commandName = content.split(" ")[0].substring(1).lowercase()
            val command = commands[commandName]
            command?.execute(event, commands)
        }
    }
}