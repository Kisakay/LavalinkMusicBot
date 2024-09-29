package org.example

import kotlinx.serialization.json.Json
import org.example.structures.Config
import java.io.InputStreamReader

val configStream = Thread.currentThread().contextClassLoader.getResourceAsStream("config.json")
    ?: throw IllegalStateException("config.json not found in resources")

val BotConfig = configStream.use { inputStream ->
    InputStreamReader(inputStream).readText()
}.let { jsonString ->
    Json.decodeFromString<Config>(jsonString)
}
