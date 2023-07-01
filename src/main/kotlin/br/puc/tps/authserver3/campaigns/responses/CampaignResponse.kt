package br.puc.tps.authserver3.campaigns.responses

import br.puc.tps.authserver3.users.User
import br.puc.tps.authserver3.users.responses.UserResponse

data class CampaignResponse (

    var id: Long,
    var title: String,
    var systemRules: String,
    var master: String,
    var maxPlayers: Long,
    var playersIn: Long,
    var playersName: List<UserResponse>

)

