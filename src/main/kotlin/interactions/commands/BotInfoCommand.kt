package org.example.interactions.commands

import dev.kord.common.Color
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import kotlinx.coroutines.flow.count
import method.getAllMembers
import org.example.MusicService

class BotInfoCommand : Command {
    override val name: String = "botinfo"
    override val description: String = "Displays the bot info embed"
    override val permissions: String = "everyone"
    override val params: String = "<>"

    override suspend fun execute(event: MessageCreateEvent, commands: Map<String, Command>, musicService: MusicService) {
        event.message.channel.createMessage {
            embeds = mutableListOf(EmbedBuilder().apply {
                title = "Informations about the bot"
                color = Color(0x1E1E14)

                fields += EmbedBuilder.Field().apply {
                    name = "Commands:"
                    value = "${commands.size}"
                    inline = true
                }
                fields += EmbedBuilder.Field().apply {
                    name = "Guilds:"
                    value = "${event.kord.guilds.count()}"
                    inline = true
                }
                fields += EmbedBuilder.Field().apply {
                    name = "Members:"
                    value = "${getAllMembers(event.kord).size}"
                    inline = true
                }


                footer = EmbedBuilder.Footer().apply {
                    text = "LavalinkMusic Bot"
                    icon = ""
                }
            })
        }
    }
}