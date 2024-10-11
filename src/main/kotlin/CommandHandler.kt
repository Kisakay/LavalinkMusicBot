package org.example

import dev.kord.core.event.message.MessageCreateEvent
import org.example.method.getBotPrefix
import org.example.method.getLanguageData
import org.example.structures.LanguageData
import org.reflections.Reflections
import kotlin.reflect.full.createInstance

lateinit var commands: Map<String, Command>

interface Command {
    val name: String
    val description: String
    val permissions: String
    val params: String
    val category: String
    val aliases: Array<String>? get() = null

    suspend fun execute(
        event: MessageCreateEvent,
        args: List<String> = listOf(),
        lang: LanguageData,
    )
}

class CommandHandler {

    fun loadCommands(): Map<String, Command> {
        val reflections = Reflections("org.example.interactions.commands")
        val commandClasses = reflections.getSubTypesOf(Command::class.java)

        val commandMap = mutableMapOf<String, Command>()
        commandClasses.mapNotNull { clazz ->
            try {
                val instance = clazz.kotlin.createInstance()
                commandMap[instance.name.lowercase()] = instance
                instance.aliases?.forEach { alias ->
                    commandMap[alias.lowercase()] = instance
                }
            } catch (e: Exception) {
                println("Failed to create instance of ${clazz.simpleName}: ${e.message}")
            }
        }

        commands = commandMap
        return commandMap
    }

    suspend fun handle(event: MessageCreateEvent) {
        val content = event.message.content
        val prefix = getBotPrefix(event.guildId.toString())

        if (content.startsWith(prefix)) {
            val commandName =
                content.removePrefix(prefix).trim().split(" ")[0].lowercase()
            val args = content.removePrefix(prefix + commandName).trim().split(" ")

            val command = commands[commandName]
            command?.execute(
                event,
                args = args,
                lang = getLanguageData(event.guildId.toString()),
            )
        }
    }
}