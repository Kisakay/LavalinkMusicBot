package org.example.interactions.commands

import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.Message
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder

class HelpCommand(private val commands: Map<String, Command>) : Command {
    override val name: String = "help"
    override val description: String = "Displays this help message"
    override val permissions: String = "everyone"

    override suspend fun execute(event: MessageCreateEvent) {
        val channel = event.message.channel
        channel.createMessage {
            embeds = mutableListOf(EmbedBuilder().apply {
                title = "Help"
                description = "List of available commands"

                commands.values.forEach { command ->
                    field {
                        name = "!${command.name}"
                        value = "${command.description}\nPermissions: ${command.permissions}"
                        inline = false
                    }
                }
            })
        }
    }
}