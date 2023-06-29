package br.puc.tps.authserver3.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties("security")
data class SecurityProperties @ConstructorBinding constructor(

    val secret: String,
    val issuer: String,
    val expireHours: Long


)
