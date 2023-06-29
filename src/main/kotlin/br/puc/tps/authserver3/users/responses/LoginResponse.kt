package br.puc.tps.authserver3.users.responses

data class LoginResponse(

    val token: String,
    val user: UserResponse

)
