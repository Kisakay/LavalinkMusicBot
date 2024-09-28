package org.example.interactions.commands

import dev.kord.core.behavior.edit
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import org.example.BotConfig
import org.example.Command
import org.example.MusicService
import org.example.iHorizonDatabase
import org.example.structures.LanguageData

class SetLangCommand : Command {
    override val name: String = "lang"
    override val description: String = "choose the bot language"
    override val permissions: String = "admin(s)"
    override val params: String = "<fr/en>"

    override suspend fun execute(
        event: MessageCreateEvent,
        args: List<String>,
        lang: LanguageData,
        musicService: MusicService
    ) {
        val lang = args[0];

        if (lang == "fr") {
            event.message.reply {
                content = "La langue du bot as été définis en Français avec succès!"
            }
        } else if (lang == "en") {
            event.message.reply {
                content = "The bot language are succesfuly set to English!"
            }
        } else {
            event.message.reply {
                content = "The available language for the bot is: `en`, `fr`"
            }
            return
        }

        iHorizonDatabase.set("${event.guildId.toString()}.language", lang)
        return
    }
}