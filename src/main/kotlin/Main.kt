package org.example

import dev.kord.core.Kord
import dev.kord.gateway.ALL
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import kotlinx.serialization.json.Json
import org.example.structures.Config
import java.io.File

import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.ClasspathHelper
import org.example.events.EventHandler

suspend fun main() {
    val classLoader = Thread.currentThread().contextClassLoader
    val configFile = File(classLoader.getResource("config.json")!!.file)

    val configFileContent = configFile.readText()

    val config = Json.decodeFromString<Config>(configFileContent)

    val kord = Kord(config.discord.token)

    val reflections = Reflections(ConfigurationBuilder()
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