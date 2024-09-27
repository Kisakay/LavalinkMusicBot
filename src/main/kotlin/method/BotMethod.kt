package method;

import dev.kord.core.Kord;
import dev.kord.core.entity.Member;
import kotlinx.coroutines.flow.toList

suspend fun getAllMembers(kord: Kord): List<Member> {
    val allMembers = mutableListOf<Member>()
    for (guild in kord.guilds.toList()) {
        allMembers.addAll(guild.members.toList())
    }
    return allMembers
}