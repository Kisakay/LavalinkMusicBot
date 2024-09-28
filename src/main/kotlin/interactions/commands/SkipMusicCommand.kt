package org.example.interactions.commands

import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.kord.getLink
import org.example.Command
import org.example.MusicService
import org.example.method.musicQueue
import org.example.method.playNextTrack
import org.example.structures.LanguageData

class SkipMusicCommand : Command {
    override val name: String = "skip"
    override val description: String = "Skip the current track"
    override val permissions: String = "everyone"
    override val params: String = "<>"

    override suspend fun execute(
        event: MessageCreateEvent,
        args: List<String>,
        lang: LanguageData,
        musicService: MusicService
    ) {
        val link = musicService.lavalink.getLink(event.message.getGuild().id)

        if (musicQueue.isEmpty()) {
            event.message.channel.createMessage(lang.queue_empty)
            return
        }

        val nextTrack = playNextTrack(link)

        val responseMessage = if (nextTrack != null) {
            "${lang.next_song}: ${nextTrack.info.title}"
        } else {
            "${lang.queue_empty2}"
        }

        event.message.channel.createMessage(responseMessage)
    }
}