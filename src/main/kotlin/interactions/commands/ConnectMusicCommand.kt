package org.example.interactions.commands

import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.kord.getLink
import org.example.MusicService

class ConnectMusicCommand : Command {
    override val name: String = "connect"
    override val description: String = "Bot join the channel"
    override val permissions: String = "everyone"
    override val params: String = "<>"

    override suspend fun execute(
        event: MessageCreateEvent,
        commands: Map<String, Command>,
        musicService: MusicService
    ) {
        val link = musicService.lavalink.getLink(event.message.getGuild().id)

        connect(event, link)
    }
}