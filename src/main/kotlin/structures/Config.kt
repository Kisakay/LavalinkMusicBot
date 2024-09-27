package org.example.structures

import kotlinx.serialization.Serializable

@Serializable
data class LavalinkNodeOptions(
    val uri: String,
    val password: String,
)

@Serializable
data class DiscordConfig(
    val token: String,
    val prefix: String
)

@Serializable
data class LavalinkConfig(
    val nodes : List<LavalinkNodeOptions>,
    val defaultSource: String,
)

@Serializable
data class Config(
    val discord: DiscordConfig,
    val lavalink: LavalinkConfig
)