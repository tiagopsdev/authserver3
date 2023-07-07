package br.puc.tps.authserver3.exception


import jakarta.persistence.Entity
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(NOT_FOUND)
class NotFoundException(
    var entity: String,
    var param: String,
    message: String? = NOT_FOUND.reasonPhrase,
    cause: Throwable? = null

): IllegalArgumentException(message, cause)