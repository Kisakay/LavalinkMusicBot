package org.example.lavalink

import dev.kord.core.Kord
import dev.schlaubi.lavakord.LavaKord
import dev.schlaubi.lavakord.kord.lavakord
import dev.schlaubi.lavakord.plugins.lavasrc.LavaSrc
import org.example.BotConfig


fun setupLavaLink(kord: Kord): LavaKord {
    val lavalink = kord.lavakord {
        plugins {
            install(LavaSrc)
        }
    }
    lavalink.addNode(BotConfig.lavalink.nodes[0].uri, BotConfig.lavalink.nodes[0].password)
    return lavalink
}