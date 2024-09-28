package org.example.interactions.commands

import dev.kord.core.behavior.edit
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import org.example.BotConfig
import org.example.Command
import org.example.MusicService
import org.example.iHorizonDatabase
import org.example.structures.LanguageData

class PrefixCommand : Command {
    override val name: String = "prefix"
    override val category: String = "bot"
    override val description: String = "customize the bot prefix"
    override val permissions: String = "admin(s)"
    override val params: String = "<prefix>"

    override suspend fun execute(
        event: MessageCreateEvent,
        args: List<String>,
        lang: LanguageData,
        musicService: MusicService
    ) {
        val prefix = args[0];

        if (prefix.length >= 5) {
            event.message.reply {
                content = lang.prefix_too_long.replace("\${prefix}", prefix)
            }
            return
        }

        iHorizonDatabase.set("${event.guildId.toString()}.prefix", prefix);

        event.message.reply {
            content = lang.prefix_set.replace("\${prefix}", prefix)
        }
        return;
    }
}