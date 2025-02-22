package org.example.interactions.commands

import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.kord.getLink
import org.example.Command
import org.example.MusicService
import org.example.method.connect
import org.example.structures.LanguageData

class ConnectMusicCommand : Command {
    override val name: String = "connect"
    override val category: String = "utils"
    override val description: String = "Bot join the channel"
    override val permissions: String = "everyone"
    override val params: String = "<>"
    override val aliases: Array<String> = arrayOf("join")

    override suspend fun execute(
        event: MessageCreateEvent,
        args: List<String>,
        lang: LanguageData,
        musicService: MusicService
    ) {
        val link = musicService.lavalink.getLink(event.message.getGuild().id)

        connect(event, link, lang)
    }
}