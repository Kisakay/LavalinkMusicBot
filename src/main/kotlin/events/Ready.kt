package org.example.events

import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.on


class BotReadyHandler : EventHandler {
    override suspend fun register(kord: Kord) {
        kord.on<ReadyEvent> {
            println("Logged in as ${self.tag} (${self.id})")
        }
    }
}