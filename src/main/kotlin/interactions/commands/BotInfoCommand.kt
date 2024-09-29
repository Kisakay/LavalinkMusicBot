package org.example.interactions.commands

import dev.kord.common.Color
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import kotlinx.coroutines.flow.count
import method.getAllMembers
import org.example.Command
import org.example.MusicService
import org.example.commands
import org.example.structures.LanguageData

class BotInfoCommand : Command {
    override val name: String = "botinfo"
    override val category: String = "bot"
    override val description: String = "Displays the bot info embed"
    override val permissions: String = "everyone"
    override val params: String = "<>"
    override val aliases: Array<String> = arrayOf("bi", "info", "bot")

    override suspend fun execute(
        event: MessageCreateEvent,
        args: List<String>,
        lang: LanguageData,
        musicService: MusicService
    ) {
        event.message.channel.createMessage {
            embeds = mutableListOf(EmbedBuilder().apply {
                title = lang.bot_info_embed_title
                color = Color(0x1E1E14)

                fields += EmbedBuilder.Field().apply {
                    name = lang.var_commands + ":"
                    value = "${commands.size}"
                    inline = true
                }
                fields += EmbedBuilder.Field().apply {
                    name = lang.var_guilds + ":"
                    value = "${event.kord.guilds.count()}"
                    inline = true
                }
                fields += EmbedBuilder.Field().apply {
                    name = lang.var_members + ":"
                    value = "${getAllMembers(event.kord).size}"
                    inline = true
                }


                footer = EmbedBuilder.Footer().apply {
                    text = "iHorizon Music"
                    icon = ""
                }
            })
        }
    }
}