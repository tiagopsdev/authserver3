package br.puc.tps.authserver3.security

data class UserToken(
    val id: Long,
    val name: String,
    val roles: Set<String>,
)
{
    constructor(): this(0, "" ,setOf())
}
