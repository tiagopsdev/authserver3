package br.puc.tps.authserver3.campaigns.requests

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CampaignRequest(
    var id: Long? = null,
    @field:NotBlank
    var title: String?,
    @field:NotBlank
    var systemRule: String?,
    @field:NotBlank
    @field:Size(min = 12, max = 48)
    //@Pattern("^[A-Za-z!@#\$%&*]+\$\n")
    var password: String?,
    @field:Min(1)
    @field:Max(9)
    @field:NotNull
    var maxPlayers: Long?,
)
