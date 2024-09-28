package org.example.method

import dev.arbjerg.lavalink.protocol.v4.Track
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.audio.Link
import org.example.structures.LanguageData

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

suspend fun connect(event: MessageCreateEvent, link: Link, lang: LanguageData) {
    val voiceState = event.member?.getVoiceState()
    val channelId = voiceState?.channelId ?: run {
        event.message.channel.createMessage(lang.connect_need_vc)
        return
    }
    link.connectAudio(channelId.value)
    val voiceChannel = event.kord.getChannelOf<VoiceChannel>(channelId)
    val channelName = voiceChannel?.name ?: lang.var_unknown
    event.message.channel.createMessage("${lang.connect_connected}: $channelName")
}