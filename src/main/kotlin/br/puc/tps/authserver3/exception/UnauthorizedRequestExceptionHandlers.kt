
package br.puc.tps.authserver3.exception


import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class UnauthorizedRequestExceptionHandlers {

    @ExceptionHandler(UnauthorizedRequestException::class)
    fun handlerURE(ex: UnauthorizedRequestException) =

        ResponseEntity.status(UNAUTHORIZED).body("Message: ${ex.message}\nlocalizedMessage: ${ex.localizedMessage}")


}
