package org.example.events

import dev.kord.common.entity.PresenceStatus
import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.on
import org.example.BotConfig


class BotReadyHandler : EventHandler {
    override suspend fun register(kord: Kord) {
        kord.on<ReadyEvent> {
            println("Logged in as ${self.tag} (${self.id})");

            kord.editPresence {
                status = PresenceStatus.Online
                listening("music on all servers! | Type: ${BotConfig.discord.prefix}help")
            }
        }
    }
}