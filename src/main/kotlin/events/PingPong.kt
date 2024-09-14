package org.example.events

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on

class MessageCreateHandler : EventHandler {
    override suspend fun register(kord: Kord) {
        kord.on<MessageCreateEvent> {
            if (message.author?.isBot == true) return@on
            if (message.content == "!ping") {
                message.channel.createMessage("Pong!")
            }
        }
    }
}