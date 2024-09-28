package org.example.method

import org.example.BotConfig
import org.example.iHorizonDatabase

fun getBotPrefix(guildId: String): String {
    return iHorizonDatabase.get("${guildId}.prefix") as? String ?: BotConfig.discord.prefix
}