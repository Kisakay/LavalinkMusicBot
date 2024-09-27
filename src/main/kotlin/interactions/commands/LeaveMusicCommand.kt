package org.example.interactions.commands

import dev.kord.core.behavior.edit
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.kord.getLink
import org.example.MusicService

class LeaveMusicCommand : Command {
    override val name: String = "leave"
    override val description: String = "bot leave the voice channel"
    override val permissions: String = "everyone"
    override val params: String = "<>"

    override suspend fun execute(
        event: MessageCreateEvent,
        commands: Map<String, Command>,
        musicService: MusicService
    ) {
        val link = musicService.lavalink.getLink(event.message.getGuild().id)
        val player = link.player

        link.destroy()
        event.message.reply { content = "Até mais!" }
    }
}