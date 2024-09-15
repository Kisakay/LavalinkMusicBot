package org.example.lavalink

import dev.kord.core.Kord
import dev.schlaubi.lavakord.LavaKord
import dev.schlaubi.lavakord.kord.lavakord
import dev.schlaubi.lavakord.plugins.lavasrc.LavaSrc
import dev.schlaubi.lavakord.plugins.sponsorblock.Sponsorblock


fun setupLavaLink(kord: Kord): LavaKord {
    val lavalink = kord.lavakord {
        plugins {
            install(LavaSrc)
            install(Sponsorblock)
        }
    }
    lavalink.addNode("", "")
    return lavalink
}