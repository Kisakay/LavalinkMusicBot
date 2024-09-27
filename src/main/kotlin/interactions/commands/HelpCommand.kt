package org.example.interactions.commands

import dev.kord.common.Color
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import org.example.BotConfig
import org.example.Command
import org.example.MusicService

class HelpCommand : Command {
    override val name: String = "help"
    override val description: String = "Displays this help message"
    override val permissions: String = "everyone"
    override val params: String = "<>"

    override suspend fun execute(
        event: MessageCreateEvent,
        commands: Map<String, Command>,
        musicService: MusicService
    ) {
        val botAvatarUrl = event.kord.getSelf().avatar?.cdnUrl?.toUrl()?.toString()
            ?: "https://cdn.discordapp.com/embed/avatars/0.png"

        event.message.reply {
            embeds = mutableListOf(EmbedBuilder().apply {
                title = "Help"
                description = "List of available commands"
                color = Color(0x00FF00)

                image = "https://ihorizon.me/assets/img/banner/ihrz_en-US.png"
                thumbnail = EmbedBuilder.Thumbnail().apply {
                    url = botAvatarUrl
                }

                commands.values.forEach { command ->
                    fields += EmbedBuilder.Field().apply {
                        name = "**`${BotConfig.discord.prefix}${command.name} ${command.params}`**"
                        value = "${command.description}\nPermissions: ${command.permissions}\nAliases: ${
                            if (command.aliases == null) "None" else command.aliases!!.map { "`$it`" }
                                .joinToString(", ")
                        }"
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