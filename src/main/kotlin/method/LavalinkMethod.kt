package org.example.method

import dev.arbjerg.lavalink.protocol.v4.Track
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.audio.Link

val musicQueue = mutableListOf<Track>();

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
        event.message.channel.createMessage("Please connect to a voice channel")
        return
    }
    link.connectAudio(channelId.value)
    val voiceChannel = event.kord.getChannelOf<VoiceChannel>(channelId)
    val channelName = voiceChannel?.name ?: "Unknown"
    event.message.channel.createMessage("Connected to the channel: $channelName")
}

suspend fun skipTrack(link: Link, event: MessageCreateEvent) {
    if (musicQueue.isEmpty()) {
        event.message.channel.createMessage("The queue is empty, there are no songs to skip")
        return
    }

    val nextTrack = playNextTrack(link)

    val responseMessage = if (nextTrack != null) {
        "Next song: ${nextTrack.info.title}"
    } else {
        "The queue is empty, there are no more songs to play!"
    }

    event.message.channel.createMessage(responseMessage)
}
