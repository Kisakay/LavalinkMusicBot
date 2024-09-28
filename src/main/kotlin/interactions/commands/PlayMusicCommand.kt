package org.example.interactions.commands

import dev.arbjerg.lavalink.protocol.v4.LoadResult
import dev.arbjerg.lavalink.protocol.v4.Track
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
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
import dev.kord.common.Color
import dev.kord.core.behavior.reply
import org.example.structures.LanguageData

class PlayMusicCommand : Command {
    override val name: String = "play"
    override val category: String = "music"
    override val description: String = "Play music into a voice channel"
    override val permissions: String = "everyone"
    override val params: String = "<link>"
    override val aliases: Array<String>? = arrayOf("p")

    override suspend fun execute(
        event: MessageCreateEvent,
        args: List<String>,
        lang: LanguageData,
        musicService: MusicService
    ) {
        val query = args.joinToString(" ")
        val search = if (query.startsWith("http")) query else "ytsearch:$query"
        val link = musicService.lavalink.getLink(event.message.getGuild().id)

        if (link.state != Link.State.CONNECTED) {
            connect(event, link, lang)
        }

        var track: Track? = null
        var embed: EmbedBuilder? = null

        val loadResult = musicService.loadTrack(link, search)

        val responseMessage = when (loadResult) {
            is LoadResult.TrackLoaded -> {
                val trackInfo = loadResult.data.info
                musicQueue.add(loadResult.data)
                if (link.player.playingTrack == null) {
                    track = playNextTrack(link)
                }

                embed = createMusicEmbed(trackInfo.title, trackInfo.uri, trackInfo.artworkUrl, lang)
                if (track == null) "${lang.play_added_to_queue}: ${trackInfo.title}"
                else "${lang.play_playing_now}: ${trackInfo.title}"
            }

            is LoadResult.PlaylistLoaded -> {
                val trackInfo = loadResult.data.tracks.first().info
                musicQueue.addAll(loadResult.data.tracks)
                if (link.player.playingTrack == null) {
                    track = playNextTrack(link)
                }

                embed = createMusicEmbed(trackInfo.title, trackInfo.uri, trackInfo.artworkUrl, lang)
                if (track == null) "${lang.play_added_to_queue}: ${trackInfo.title}"
                else "${lang.play_playing_now}: ${trackInfo.title}"
            }

            is LoadResult.SearchResult -> {
                val trackInfo = loadResult.data.tracks.first().info
                musicQueue.add(loadResult.data.tracks.first())
                if (link.player.playingTrack == null) {
                    track = playNextTrack(link)
                }

                embed = createMusicEmbed(trackInfo.title, trackInfo.uri, trackInfo.artworkUrl, lang)
                if (track == null) "${lang.play_added_to_queue}: ${trackInfo.title}"
                else "${lang.play_playing_now}: ${trackInfo.title}"
            }

            is LoadResult.NoMatches -> "${lang.no_songs_found}"
            is LoadResult.LoadFailed -> loadResult.data.message ?: "${lang.play_error_loading}"
        }

        if (embed != null) {
            event.message.reply {
                embeds = mutableListOf(embed)
            }
        } else {
            event.message.channel.createMessage(responseMessage)
        }
    }

    // Helper function to create an embed
    private fun createMusicEmbed(title: String, url: String?, thumbnailUrl: String?, lang: LanguageData): EmbedBuilder {
        return EmbedBuilder().apply {
            this.title = title
            this.url = url
            description = "${lang.play_playing_now}: [$title]($url)"
            color = Color(0x1DB954)

            if (thumbnailUrl != null) {
                image = thumbnailUrl
            }

            footer {
                text = "iHorizon Music"
            }
        }
    }
}