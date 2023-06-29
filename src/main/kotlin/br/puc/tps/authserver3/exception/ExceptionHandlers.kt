package br.puc.tps.authserver3.exception

import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlers {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handlerValidationException(ex: MethodArgumentNotValidException) =
        ex.allErrors
            .joinToString("\n") {"'${(it as FieldError).field}': ${it.defaultMessage}"}
            .let { ResponseEntity.badRequest().body(it)}
}


