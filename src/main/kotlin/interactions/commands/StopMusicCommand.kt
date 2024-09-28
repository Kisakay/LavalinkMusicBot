package org.example.interactions.commands

import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.kord.getLink
import org.example.Command
import org.example.MusicService
import org.example.structures.LanguageData

class StopMusicCommand : Command {
    override val name: String = "stop"
    override val description: String = "pause the current track"
    override val permissions: String = "everyone"
    override val params: String = "<>"

    override suspend fun execute(
        event: MessageCreateEvent,
        args: List<String>,
        lang: LanguageData,
        musicService: MusicService
    ) {
        val link = musicService.lavalink.getLink(event.message.getGuild().id)
        val player = link.player

        player.stopTrack()
        event.message.reply { content = lang.stop_message }
    }
}