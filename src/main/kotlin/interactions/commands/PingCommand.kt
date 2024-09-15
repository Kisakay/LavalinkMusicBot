package org.example.interactions.commands

// PingCommand.kt
import dev.kord.core.event.message.MessageCreateEvent

class PingCommand : org.example.interactions.commands.Command {
    override suspend fun execute(event: MessageCreateEvent) {
        event.message.channel.createMessage("Pong!")
    }
}