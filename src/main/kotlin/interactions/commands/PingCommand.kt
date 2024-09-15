package org.example.interactions.commands

// PingCommand.kt
import dev.kord.core.event.message.MessageCreateEvent

class PingCommand : Command {
    override val name: String = "ping"
    override val description: String = "Responds with Pong!"
    override val permissions: String = "everyone"

    override suspend fun execute(event: MessageCreateEvent) {
        event.message.channel.createMessage("Pong!")
    }
}