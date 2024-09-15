package org.example.interactions.commands

import dev.kord.core.event.message.MessageCreateEvent
import org.reflections.Reflections
import kotlin.reflect.full.createInstance

interface Command {
    suspend fun execute(event: MessageCreateEvent)
}

class CommandHandler {

    private val commands: Map<String, Command> = loadCommands()

    private fun loadCommands(): Map<String, Command> {
        val reflections = Reflections("org.example.interactions.commands")
        // Find all classes that implement Command interface
        val commandClasses = reflections.getSubTypesOf(Command::class.java)
        return commandClasses.mapNotNull { clazz ->
            // Create an instance of each command
            val instance = clazz.kotlin.runCatching { createInstance() }.getOrNull()
            if (instance != null) {
                // Map command name (lowercase class name) to instance
                clazz.simpleName!!.lowercase().replace("command", "") to instance
            } else {
                null
            }
        }.toMap()
    }

    suspend fun handle(event: MessageCreateEvent) {
        val content = event.message.content
        if (content.startsWith("!")) {
            val commandName = content.split(" ")[0].substring(1).lowercase()
            val command = commands[commandName]
            command?.execute(event)
        }
    }
}