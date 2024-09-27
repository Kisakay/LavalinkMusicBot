package org.example

import dev.kord.core.Kord
import dev.kord.gateway.ALL
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import dev.schlaubi.lavakord.LavaKord
import org.example.events.EventHandler
import org.example.lavalink.setupLavaLink
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

lateinit var lavalink: LavaKord

suspend fun main() {

    val kord = Kord(BotConfig.discord.token)
    lavalink = setupLavaLink(kord);
    setupLavaLink(kord)

    val reflections = Reflections(
        ConfigurationBuilder()
            .setUrls(ClasspathHelper.forPackage("org.example.events"))
            .setScanners(SubTypesScanner())
    )
    val handlers = reflections.getSubTypesOf(EventHandler::class.java)

    handlers.forEach { handlerClass ->
        val handlerInstance = handlerClass.getDeclaredConstructor().newInstance()
        handlerInstance.register(kord)
    }

    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents = Intents.ALL
    }
}