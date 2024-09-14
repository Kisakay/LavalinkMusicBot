package org.example.events

import dev.kord.core.Kord

interface EventHandler {
    suspend fun register(kord: Kord)
}