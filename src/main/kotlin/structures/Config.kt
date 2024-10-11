package org.example.structures

import kotlinx.serialization.Serializable

@Serializable
data class DiscordConfig(
    val token: String,
    val prefix: String
)

@Serializable
data class Config(
    val discord: DiscordConfig,
)