package org.example.interactions.commands

import dev.arbjerg.lavalink.protocol.v4.LoadResult
import dev.arbjerg.lavalink.protocol.v4.Track
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.audio.Link
import dev.schlaubi.lavakord.audio.TrackEndEvent
import dev.schlaubi.lavakord.audio.on
import dev.schlaubi.lavakord.kord.getLink
import org.example.MusicService

//val musicQueue = mutableListOf<Track>()

suspend fun playNextTrack(link: Link): Track? {
    return if (musicQueue.isNotEmpty()) {
        val nextTrack = musicQueue.removeAt(0)
        link.player.playTrack(nextTrack)
        nextTrack
    } else {
        null
    }
}

suspend fun connect(event: MessageCreateEvent, link: Link) {
    val voiceState = event.member?.getVoiceState()
    val channelId = voiceState?.channelId ?: run {
        event.message.channel.createMessage("Por favor, conecte-se a um canal de voz")
        return
    }
    link.connectAudio(channelId.value)
    val voiceChannel = event.kord.getChannelOf<VoiceChannel>(channelId)
    val channelName = voiceChannel?.name ?: "Desconhecido"
    event.message.channel.createMessage("Conectado ao canal: $channelName")
}

suspend fun skipTrack(link: Link, event: MessageCreateEvent) {
    if (musicQueue.isEmpty()) {
        event.message.channel.createMessage("A fila está vazia, não há músicas para pular")
        return
    }

    val nextTrack = playNextTrack(link)

    val responseMessage = if (nextTrack != null) {
        "Próxima música: ${nextTrack.info.title}"
    } else {
        "A fila está vazia, não há mais músicas para tocar!"
    }

    event.message.channel.createMessage(responseMessage)
}

class PlayMusicCommands : Command {
    override val name: String = "play"
    override val description: String = "Play music into a voice channel"
    override val permissions: String = "everyone"
    override val params: String = "<link>"

     suspend fun execute(
        event: messagecreateevent,
        commands: map<string, command>,
        musicservice: musicservice
    ) {
        var link = event.message.content.split(" ")
        val musicService = musicService.lavalink.getLink(event.guildId)
        val search = if (query.startsWith("http")) query else "ytsearch:$query"

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
                if (track == null) "Adicionado à fila: ${item.data.info.title}"
                else "Tocando agora: ${item.data.info.title}"
            }

            is LoadResult.PlaylistLoaded -> {
                musicQueue.addAll(item.data.tracks)
                if (link.player.playingTrack == null) {
                    track = playNextTrack(link)
                }
                if (track == null) "Adicionado à fila: ${item.data.tracks.first().info.title}"
                else "Tocando agora: ${item.data.tracks.first().info.title}"
            }

            is LoadResult.SearchResult -> {
                musicQueue.add(item.data.tracks.first())
                if (link.player.playingTrack == null) {
                    track = playNextTrack(link)
                }
                if (track == null) "Adicionado à fila: ${item.data.tracks.first().info.title}"
                else "Tocando agora: ${item.data.tracks.first().info.title}"
            }

            is LoadResult.NoMatches -> "Nenhuma música encontrada"
            is LoadResult.LoadFailed -> item.data.message ?: "Erro ao carregar a música"
        }

        event.message.channel.createMessage(responseMessage)
    }
}

object MusicCommands {
    suspend fun handleMusicCommands(event: MessageCreateEvent, command: String, query: String?) {
        val guildId = event.message.getGuild().id
        val link = MusicService.lavalink.getLink(guildId)
        val player = link.player

        player.on<TrackEndEvent> {
            val track = playNextTrack(link)
            val message = if (track != null) "Tocando agora: ${track.info.title}" else "A fila de música está vazia :("
            event.message.channel.createMessage(message)
        }
    }


}