package org.example.interactions.commands

import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.kord.getLink
import org.example.Command
import org.example.MusicService
import org.example.structures.LanguageData

class PauseMusicCommand : Command {
    override val name: String = "pause"
    override val description: String = "pause the current track"
    override val permissions: String = "everyone"
    override val params: String = "<>"
    override val aliases: Array<String> = arrayOf("resume")

    override suspend fun execute(
        event: MessageCreateEvent,
        lang: LanguageData,
        commands: Map<String, Command>,
        musicService: MusicService
    ) {
        val link = musicService.lavalink.getLink(event.message.getGuild().id)
        val player = link.player

        player.pause(!player.paused)
        event.message.reply { content = if (player.paused) lang.pause_paused else lang.pause_unpaused }
    }
}