package br.puc.tps.authserver3.exception


import jakarta.persistence.Entity
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(FORBIDDEN)
class ForbiddenException(
    var action: String,
    message: String? = FORBIDDEN.reasonPhrase,
    cause: Throwable? = null

): IllegalArgumentException(message, cause)