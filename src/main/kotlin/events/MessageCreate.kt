package org.example.events

import org.example.CommandHandler

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on

class MessageCreate : EventHandler {
    override suspend fun register(kord: Kord) {
        kord.on<MessageCreateEvent> {
            val commandHandler = CommandHandler()
            commandHandler.handle(this);
        }
    }
}