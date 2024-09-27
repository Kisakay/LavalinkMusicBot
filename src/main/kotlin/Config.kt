package org.example

import kotlinx.serialization.json.Json
import org.example.structures.Config
import java.io.File

val configFile = File(Thread.currentThread()
    .contextClassLoader.getResource("config.json")!!.file);

var BotConfig = Json.decodeFromString<Config>(configFile.readText());