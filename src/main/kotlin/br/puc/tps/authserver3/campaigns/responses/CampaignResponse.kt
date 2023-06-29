package br.puc.tps.authserver3.campaigns.responses

data class CampaignResponse (

    var id: Long,
    var title: String,
    var systemRules: String,
    var maxPlayers: Long,

)