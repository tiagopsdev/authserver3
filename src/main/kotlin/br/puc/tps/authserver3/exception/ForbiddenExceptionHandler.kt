package br.puc.tps.authserver3.exception


import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ForbiddenExceptionHandler {

    @ExceptionHandler(ForbiddenException::class)
    fun handlerNFE(ex: ForbiddenException) =
        ResponseEntity.status(NOT_FOUND).body(
            ExceptionsForbiddenReponse(
                action = ex.action,
                message = ex.message)
        )

}