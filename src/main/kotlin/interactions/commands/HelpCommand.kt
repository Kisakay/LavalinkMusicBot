package org.example.interactions.commands

import dev.kord.common.Color
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.example.BotConfig
import org.example.Command
import org.example.MusicService
import org.example.commands
import org.example.method.getBotPrefix
import org.example.structures.LanguageData

class HelpCommand : Command {
    override val name: String = "help"
    override val category: String = "bot"
    override val description: String = "Displays this help message"
    override val permissions: String = "everyone"
    override val params: String = "<>"
    override val aliases: Array<String> = arrayOf("menue", "aide")

    override suspend fun execute(
        event: MessageCreateEvent,
        args: List<String>,
        lang: LanguageData,
        musicService: MusicService
    ) {
        val botAvatarUrl = event.kord.getSelf().avatar?.cdnUrl?.toUrl()
            ?: event.kord.getSelf().defaultAvatar.cdnUrl.toUrl()

        event.message.reply {
            embeds = mutableListOf(EmbedBuilder().apply {
                title = "Help"
                description = lang.help_embed_title
                color = Color(0x00FF00)

                image = "https://ihorizon.me/assets/img/banner/ihrz_en-US.png"
                thumbnail = EmbedBuilder.Thumbnail().apply {
                    url = botAvatarUrl
                }

                commands.values.distinctBy { it.name }.forEach { command ->
                    fields += EmbedBuilder.Field().apply {
                        name = "**`${getBotPrefix(event.guildId.toString())}${command.name} ${command.params}`**"
                        value =
                            "${command.description}\n${lang.var_perm}: ${command.permissions}\n${lang.var_aliases}: ${
                                if (command.aliases.isNullOrEmpty()) lang.var_none
                                else command.aliases!!.joinToString(", ") { "`$it`" }
                            }"
                        inline = false
                    }
                }

                footer = EmbedBuilder.Footer().apply {
                    text = "iHorizon Music"
                    icon = ""
                }

                timestamp = Clock.System.now()
            })
        }
    }
}