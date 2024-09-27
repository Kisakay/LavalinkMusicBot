package org.example.interactions.commands

import dev.arbjerg.lavalink.protocol.v4.LoadResult
import dev.arbjerg.lavalink.protocol.v4.Track
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.audio.Link
import dev.schlaubi.lavakord.audio.TrackEndEvent
import dev.schlaubi.lavakord.audio.on
import dev.schlaubi.lavakord.kord.getLink
import org.example.BotConfig
import org.example.Command
import org.example.MusicService
import org.example.method.connect
import org.example.method.musicQueue
import org.example.method.playNextTrack

class PlayMusicCommand : Command {
    override val name: String = "play"
    override val description: String = "Play music into a voice channel"
    override val permissions: String = "everyone"
    override val params: String = "<link>"
    override val aliases: Array<String>? = arrayOf("p")

    override suspend fun execute(
        event: MessageCreateEvent,
        commands: Map<String, Command>,
        musicService: MusicService
    ) {
        val query = event.message.content.removePrefix("${BotConfig.discord.prefix}play").trim()
        val search = if (query.startsWith("http")) query else "ytsearch:$query"
        val link = musicService.lavalink.getLink(event.message.getGuild().id);

        if (link.state != Link.State.CONNECTED) {
            connect(event, link)
        }

        var track: Track? = null
        val responseMessage = when (val item = musicService.loadTrack(link, search)) {
            is LoadResult.TrackLoaded -> {
                musicQueue.add(item.data)
                if (link.player.playingTrack == null) {
                    track = playNextTrack(link)
                }
                if (track == null) "Added to queue: ${item.data.info.title}"
                else "Playing now: ${item.data.info.title}"
            }

            is LoadResult.PlaylistLoaded -> {
                musicQueue.addAll(item.data.tracks)
                if (link.player.playingTrack == null) {
                    track = playNextTrack(link)
                }
                if (track == null) "Added to queue: ${item.data.tracks.first().info.title}"
                else "Playing now: ${item.data.tracks.first().info.title}"
            }

            is LoadResult.SearchResult -> {
                musicQueue.add(item.data.tracks.first())
                if (link.player.playingTrack == null) {
                    track = playNextTrack(link)
                }
                if (track == null) "Added to queue: ${item.data.tracks.first().info.title}"
                else "Playing now: ${item.data.tracks.first().info.title}"
            }

            is LoadResult.NoMatches -> "No songs found"
            is LoadResult.LoadFailed -> item.data.message ?: "Error loading music"
        }

        event.message.channel.createMessage(responseMessage)
    }
}