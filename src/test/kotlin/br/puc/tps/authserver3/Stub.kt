package br.puc.tps.authserver3

import br.puc.tps.authserver3.UserStubs.userStub
import br.puc.tps.authserver3.users.Role
import br.puc.tps.authserver3.users.User
import br.puc.tps.authserver3.campaigns.Campaign
import br.puc.tps.authserver3.systemrules.SystemRule

import kotlin.random.Random



object UserStubs {
    fun userStub(
        id: Long? = Random.nextLong(1, 1000),
        roles: List<String> = listOf("USER")
    ): User {
        val name = "user-${id ?: "new"}"
        return User(
            id = id,
            name = name,
            email = "$name@email.com",
            password = randomString(),
            roles = roles
                .mapIndexed { i, it -> Role(i.toLong(), it) }
                .toMutableSet()
        )
    }
}

object CampaignStubs {
    fun campaignStub(
        id: Long? = Random.nextLong(1, 1000),
        qtdusers: Int = Random.nextInt(1,9)
    ): Campaign {

        var qu = qtdusers
        var users = mutableSetOf<User>()
        if (qu == 0) {qu = 1}
        for (userid in List(qu) { index -> index }) {
            users.add(userStub(userid.toLong()))
        }
        return Campaign(
            id = id,
            title = "${id?:1} - ${ramdomQuest(id?:1)}}",
            systemRule = SystemRule(1L, "Proprio"),
            master = userStub(id),
            password = randomString(length = 10),
            maxPlayers = Random.nextLong(1,9),
            users = users


        )
    }
}
