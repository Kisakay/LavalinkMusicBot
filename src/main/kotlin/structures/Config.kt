package org.example.structures

import kotlinx.serialization.Serializable

@Serializable
data class DiscordConfig(
    val token: String,
    val prefix: String
)

@Serializable
data class LavalinkNodeConfig(
    val uri: String,
    val password: String
)

@Serializable
data class LavalinkConfig(
    val nodes: List<LavalinkNodeConfig> = emptyList(),
    val defaultSource: String = "youtube"
)

@Serializable
data class Config(
    val discord: DiscordConfig,
    val lavalink: LavalinkConfig = LavalinkConfig(),
)
