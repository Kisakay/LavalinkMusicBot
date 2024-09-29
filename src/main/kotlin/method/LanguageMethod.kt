package org.example.method

import org.example.iHorizonDatabase
import org.example.structures.LanguageData
import org.yaml.snakeyaml.Yaml
import java.io.InputStream
import kotlin.IllegalArgumentException

fun getLanguageData(guildId: String): LanguageData {
    val language = iHorizonDatabase.get("${guildId}.language") ?: "en"

    val inputStream: InputStream = Thread.currentThread().contextClassLoader.getResourceAsStream("lang/${language}.yml")
        ?: throw IllegalArgumentException("${language}.yml not found in resources")

    val yaml = Yaml()

    return inputStream.use { stream ->
        yaml.loadAs(stream, LanguageData::class.java)
    }
}
