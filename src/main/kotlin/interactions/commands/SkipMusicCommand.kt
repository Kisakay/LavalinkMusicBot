package org.example.interactions.commands

import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.kord.getLink
import org.example.Command
import org.example.MusicService
import org.example.method.skipTrack

class SkipMusicCommand : Command {
    override val name: String = "skip"
    override val description: String = "Skip the current track"
    override val permissions: String = "everyone"
    override val params: String = "<>"

    override suspend fun execute(event: MessageCreateEvent, commands: Map<String, Command>, musicService: MusicService) {
        val link = musicService.lavalink.getLink(event.message.getGuild().id)

        skipTrack(link, event)
    }
}