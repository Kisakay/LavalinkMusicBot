package org.example.method

import org.example.structures.LanguageData
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.InputStream

fun getLanguageData(guildId: String): LanguageData {
    val language = when (guildId) {
        "1234567890" -> "fr"
        "0987654321" -> "en"
        else -> "en"
    }

    val file = Thread.currentThread().contextClassLoader.getResource("lang/${language}.yml")?.file
        ?: throw IllegalArgumentException("${language}.yml not found")

    val inputStream: InputStream = File(file).inputStream()

    val yaml = Yaml()

    return yaml.loadAs(inputStream, LanguageData::class.java)
}
