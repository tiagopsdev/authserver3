package br.puc.tps.authserver3.exception

import io.jsonwebtoken.MalformedJwtException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GetSelfExceptionHandlers {

    @ExceptionHandler(GetSelfException::class)
    fun GetSelfWOLogin(ex: GetSelfException) =
        ResponseEntity.badRequest().body("Message: GetSelfExceptionHandlers")


}