package org.example.interactions.commands

import dev.kord.common.entity.Permission
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import org.example.Command
import org.example.iHorizonDatabase
import org.example.method.getLanguageData
import org.example.structures.LanguageData

class SetLangCommand : Command {
    override val name: String = "lang"
    override val category: String = "bot"
    override val description: String = "choose the bot language"
    override val permissions: String = "admin(s)"
    override val params: String = "<fr/en>"

    override suspend fun execute(
        event: MessageCreateEvent,
        args: List<String>,
        lang: LanguageData,
    ) {
        if (!event.member?.getPermissions()!!.contains(Permission.Administrator)) {
            return
        }

        val lang2 = args[0];

        if (lang2 == "fr") {
            iHorizonDatabase.set("${event.guildId.toString()}.language", lang2)
            val lang3 = getLanguageData(event.guildId.toString());

            event.message.reply {
                content = lang3.lang_set_sucessfuly
            }
        } else if (lang2 == "en") {
            iHorizonDatabase.set("${event.guildId.toString()}.language", lang2)
            val lang3 = getLanguageData(event.guildId.toString());

            event.message.reply {
                content = lang3.lang_set_sucessfuly
            }
        } else {
            event.message.reply {
                content = lang.lang_available
            }
            return
        }
    }
}