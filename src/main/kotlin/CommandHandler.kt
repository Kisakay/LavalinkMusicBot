package org.example

import dev.kord.core.event.message.MessageCreateEvent
import org.reflections.Reflections
import kotlin.reflect.full.createInstance

interface Command {
    val name: String
    val description: String
    val permissions: String
    val params: String
    val aliases: Array<String>? get() = null

    suspend fun execute(event: MessageCreateEvent, commands: Map<String, Command> = mapOf(), musicService: MusicService)
}

class CommandHandler {

    private val commands: Map<String, Command> = loadCommands()

    private fun loadCommands(): Map<String, Command> {
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
        return commandMap
    }


    suspend fun handle(event: MessageCreateEvent) {
        val content = event.message.content
        if (content.startsWith(BotConfig.discord.prefix)) {
            val commandName = content.split(" ")[0].substring(1).lowercase()
            val command = commands[commandName]
            command?.execute(event, commands, musicService = MusicService(org.example.lavalink))
        }
    }
}