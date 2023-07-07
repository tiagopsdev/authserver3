package br.puc.tps.authserver3.exception


import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class NotFoundExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun handlerNFE(ex: NotFoundException) =
        ResponseEntity.status(NOT_FOUND).body(
            ExceptionsNotFoundReponse(
                entity = ex.entity,
                param = ex.param,
                message = ex.message,)

        )

}