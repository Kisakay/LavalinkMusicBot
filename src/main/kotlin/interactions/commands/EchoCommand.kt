package org.example.interactions.commands

import dev.kord.core.behavior.edit
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import org.example.Command
import org.example.MusicService
import org.example.structures.LanguageData

class EchoCommand : Command {
    override val name: String = "say"
    override val category: String = "bot"
    override val description: String = "the bot send the message"
    override val permissions: String = "admin(s)"
    override val params: String = "<the message>"

    override suspend fun execute(
        event: MessageCreateEvent,
        args: List<String>,
        lang: LanguageData,
        musicService: MusicService
    ) {
        val msg = args.joinToString(" ");

        event.message.channel.createMessage(
            content = msg
        )
    }
}