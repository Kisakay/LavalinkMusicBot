package org.example.interactions.commands

// PingCommand.kt
import dev.kord.core.event.message.MessageCreateEvent

class PingCommand : Command {
    override val name: String = "ping"
    override val description: String = "Responds with Pong!"
    override val permissions: String = "everyone"
    override val params: String = "<>"

    override suspend fun execute(event: MessageCreateEvent, commands: Map<String, Command>) {
        event.message.channel.createMessage("Pong!")
    }
}