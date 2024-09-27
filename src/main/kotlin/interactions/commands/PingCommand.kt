package org.example.interactions.commands

import dev.kord.core.behavior.edit
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import org.example.MusicService

class PingCommand : Command {
    override val name: String = "ping"
    override val description: String = "Responds with Pong!"
    override val permissions: String = "everyone"
    override val params: String = "<>"

    override suspend fun execute(event: MessageCreateEvent, commands: Map<String, Command>, musicService: MusicService) {
        val startTimestamp = System.currentTimeMillis()

        val responseMessage = event.message.reply { content = "⏳" }

        val endTimestamp = System.currentTimeMillis()
        val latency = endTimestamp - startTimestamp

        responseMessage.edit {
            content = "\uD83C\uDFD3 Latency: ${latency}ms"
        }
    }
}