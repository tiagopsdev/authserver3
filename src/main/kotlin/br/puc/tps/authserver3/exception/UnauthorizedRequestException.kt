package br.puc.tps.authserver3.exception


import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(UNAUTHORIZED)
class UnauthorizedRequestException(
    message: String? = UNAUTHORIZED.reasonPhrase,
    cause: Throwable? = null

): IllegalArgumentException(message, cause)