package org.example.interactions.commands

import dev.kord.core.behavior.edit
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import org.example.Command
import org.example.MusicService
import org.example.structures.LanguageData

class PingCommand : Command {
    override val name: String = "ping"
    override val category: String = "bot"
    override val description: String = "Responds with Pong!"
    override val permissions: String = "everyone"
    override val params: String = "<>"
    override val aliases: Array<String> = arrayOf("speed")

    override suspend fun execute(
        event: MessageCreateEvent,
        args: List<String>,
        lang: LanguageData,
        musicService: MusicService
    ) {
        val startTimestamp = System.currentTimeMillis()

        val responseMessage = event.message.reply { content = "⏳" }

        val endTimestamp = System.currentTimeMillis()
        val latency = endTimestamp - startTimestamp

        responseMessage.edit {
            content = "\uD83C\uDFD3 ${lang.ping_latency}: ${latency}ms"
        }
    }
}