package org.example.interactions.commands

import dev.kord.common.Color
import dev.kord.common.entity.ButtonStyle
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.interaction.response.edit
import dev.kord.core.behavior.interaction.updateEphemeralMessage
import dev.kord.core.behavior.reply
import dev.kord.core.entity.interaction.ButtonInteraction
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import kotlinx.datetime.Clock
import org.example.Command
import org.example.commands
import org.example.method.getBotPrefix
import org.example.structures.LanguageData
import dev.kord.core.on
import dev.kord.rest.builder.message.actionRow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HelpCommand : Command {
    override val name: String = "help"
    override val category: String = "bot"
    override val description: String = "Displays this help message"
    override val permissions: String = "everyone"
    override val params: String = "<>"
    override val aliases: Array<String> = arrayOf("menu", "aide")

    private var currentPage = 0

    override suspend fun execute(
        event: MessageCreateEvent,
        args: List<String>,
        lang: LanguageData,
    ) {
        val categories = commands.values.distinctBy { it.name }.groupBy { it.category }
        val totalPages = categories.size
        val botAvatarUrl = event.kord.getSelf().avatar?.cdnUrl?.toUrl()
            ?: event.kord.getSelf().defaultAvatar.cdnUrl.toUrl()

        val initialCategory = categories.keys.elementAtOrNull(currentPage) ?: "bot"
        val embedMessage = createEmbed(categories, initialCategory, botAvatarUrl, lang, event)

        val replyMessage = event.message.reply {
            embeds = mutableListOf(embedMessage)
            actionRow {
                interactionButton(style = ButtonStyle.Secondary, customId = "prev") {
                    label = "◀"
                    disabled = true
                }
                interactionButton(style = ButtonStyle.Secondary, customId = "next") {
                    label = "▶"
                }
            }
        }

        val helpMessageId = replyMessage.id

        event.kord.launch {
            delay(100_000)
            replyMessage.edit {
                actionRow {
                    interactionButton(style = ButtonStyle.Secondary, customId = "prev") {
                        label = "◀"
                        disabled = true
                    }
                    interactionButton(style = ButtonStyle.Secondary, customId = "next") {
                        label = "▶"
                        disabled = true
                    }
                }
            }
        }

        event.kord.on<InteractionCreateEvent> {
            val interaction = this.interaction as? ButtonInteraction ?: return@on

            if (interaction.message?.id != helpMessageId) return@on

            val customId = interaction.componentId

            when (customId) {
                "next" -> {
                    if (currentPage < totalPages - 1) currentPage++
                }

                "prev" -> {
                    if (currentPage > 0) currentPage--
                }
            }

            val newCategory = categories.keys.elementAtOrNull(currentPage) ?: "bot"
            val newEmbed = createEmbed(categories, newCategory, botAvatarUrl, lang, event)

            interaction.updateEphemeralMessage { }

            replyMessage.edit {
                embeds = mutableListOf(newEmbed)
                actionRow {
                    interactionButton(style = ButtonStyle.Secondary, customId = "prev") {
                        label = "◀"
                        disabled = currentPage == 0
                    }
                    interactionButton(style = ButtonStyle.Secondary, customId = "next") {
                        label = "▶"
                        disabled = currentPage == totalPages - 1
                    }
                }
            }
        }
    }

    private fun createEmbed(
        categories: Map<String, List<Command>>,
        category: String,
        botAvatarUrl: String,
        lang: LanguageData,
        event: MessageCreateEvent
    ): EmbedBuilder {
        return EmbedBuilder().apply {
            title = "Help - $category"
            description = lang.help_embed_title
            color = Color(0x00FF00)
            thumbnail = EmbedBuilder.Thumbnail().apply {
                url = botAvatarUrl
            }

            categories[category]?.forEach { command ->
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
                text = "iHorizon Music - Page ${currentPage + 1}/${categories.size}"
                icon = ""
            }

            timestamp = Clock.System.now()
        }
    }
}
