package br.puc.tps.authserver3.users.requests

import jakarta.validation.constraints.NotBlank

data class LoginRequest(

    @NotBlank
    var email: String?,
    @NotBlank
    var password: String?

)
