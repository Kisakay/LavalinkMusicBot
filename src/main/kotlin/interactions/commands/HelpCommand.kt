package org.example.interactions.commands

import dev.kord.common.Color
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder

class HelpCommand : Command {
    override val name: String = "help"
    override val description: String = "Displays this help message"
    override val permissions: String = "everyone"
    override val params: String = "<>"

    override suspend fun execute(event: MessageCreateEvent, commands: Map<String, Command>) {
        val channel = event.message.channel

        println(commands)
        channel.createMessage {
            embeds = mutableListOf(EmbedBuilder().apply {
                title = "Help"
                description = "List of available commands"
                color = Color(0x00FF00)

                commands.values.forEach { command ->
                    fields += EmbedBuilder.Field().apply {
                        name = "**`!${command.name} ${command.params}`**"
                        value = "${command.description}\nPermissions: ${command.permissions}"
                        inline = false
                    }
                }

                footer = EmbedBuilder.Footer().apply {
                    text = "LavalinkMusic Bot"
                    icon = ""
                }
            })
        }
    }
}