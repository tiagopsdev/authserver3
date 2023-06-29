package br.puc.tps.authserver3.exception

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(BAD_REQUEST)
class GetSelfException(
    message: String? = "GetSelfException",
    cause: Throwable? = null

): IllegalArgumentException(message, cause)