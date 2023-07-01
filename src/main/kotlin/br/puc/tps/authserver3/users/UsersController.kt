package br.puc.tps.authserver3.users

import br.puc.tps.authserver3.exception.GetSelfException
import br.puc.tps.authserver3.users.requests.LoginRequest
import br.puc.tps.authserver3.users.requests.UserRequest
import br.puc.tps.authserver3.users.responses.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.CONTINUE
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UsersController(val service: UsersService) {

    @Operation(summary = "Lista todos os usuários de todos os papeis opu filtrado por papel")
    @GetMapping
    fun listUsers(@RequestParam("role") role: String?) = service.findAll(role).map { it.toResponse() }

    @Transactional
    @PostMapping
    fun createUser(@RequestBody @Valid req: UserRequest) =
        //val user = User(email = req.email!!, password = req.password!!, name = req.name ?: req.email )
        service.save(req)
            .toResponse()
            .let { ResponseEntity.status(CREATED).body(it) }


    /* Long Way
    @GetMapping("/{id}")
    fun getUser(@PathVariable("id") id: Long): ResponseEntity<UserResponse>{

        val user = service.getById(id)
        return if( user == null) ResponseEntity.notFound().build()
        else ResponseEntity.ok(user.toResponse())

    }*/

    @GetMapping("/me")
    @PreAuthorize("permitAll()")
    @SecurityRequirement(name = "authserver3")
    fun getSelf(auth: Authentication) = getUser(auth.credentials as Long)


        //Kotling Way
    @GetMapping("/{id}")
    fun getUser(@PathVariable("id") id: Long) = service.getById(id).orElse(null)
        ?.let { ResponseEntity.ok(it.toResponse()) }
        ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("permitAll()")
    @SecurityRequirement(name = "authserver3")
    fun deleteUser(@PathVariable("id") id: Long): ResponseEntity<out Any> {

        return when (service.deleteUser(id)) {
            0 -> { ResponseEntity.ok().build()}
            -1 -> { ResponseEntity.notFound().build()}
            else -> { ResponseEntity.status(CONTINUE).body("Unknow state${id}")}
        }

    }
    @Transactional
    @PostMapping("/login")
    fun login(@RequestBody @Valid credentials: LoginRequest): ResponseEntity<out Any>{
        return service.login(credentials)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(UNAUTHORIZED).build()

    }

}