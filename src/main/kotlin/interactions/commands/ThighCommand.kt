package org.example.interactions.commands

import dev.kord.core.behavior.reply
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.example.Command
import org.example.structures.LanguageData
import kotlinx.serialization.Serializable
import java.lang.Error


class ThighCommand : Command {
    override val name: String = "thigh"
    override val category: String = "nsfw"
    override val description: String = "The bot sends the message."
    override val permissions: String = "everyone"
    override val params: String = "<>"

    private val httpClient = HttpClient(CIO)

    override suspend fun execute(
        event: MessageCreateEvent,
        args: List<String>,
        lang: LanguageData,
    ) {
        val channel = event.message.channel.asChannel() as? TextChannel
        if (channel == null || !channel.isNsfw) {
            event.message.channel.createMessage("This command can only be used in NSFW channels.")
            return
        }


        val apiUrl = "https://nekobot.xyz/api/image?type=thigh"
        val apiResponse: String = withContext(Dispatchers.IO) {
            httpClient.get(apiUrl).bodyAsText()
        }
        val nsfwPng: NsfwContent2 = Json.decodeFromString(apiResponse)


        val embed = EmbedBuilder().apply {
            title = "NSFW Content"
            description = "Here is the NSFW content you requested."
            image = nsfwPng.message
        }
        // Send the embed with the NSFW image
        event.message.reply {
            embeds = mutableListOf(embed)
        }
    }
}
