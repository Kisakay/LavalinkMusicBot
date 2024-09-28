package org.example.events

import org.example.CommandHandler

import dev.kord.core.Kord
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import org.example.method.getBotPrefix

class MessageCreate : EventHandler {
    override suspend fun register(kord: Kord) {
        val commandHandler = CommandHandler();
        commandHandler.loadCommands();

        kord.on<MessageCreateEvent> {
            if (message.content == "<@${message.kord.getSelf().id.toString()}>") {
                message.reply {
                    content = "My prefix on this server is ${getBotPrefix(message.getGuild().id.toString())}"
                }
            } else {
                commandHandler.handle(this);
            }
        }
    }
}