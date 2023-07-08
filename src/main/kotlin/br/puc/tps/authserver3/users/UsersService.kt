package br.puc.tps.authserver3.users

import br.puc.tps.authserver3.exception.BadRequestException
import br.puc.tps.authserver3.security.Jwt
import br.puc.tps.authserver3.users.requests.LoginRequest
import br.puc.tps.authserver3.users.requests.UserRequest
import br.puc.tps.authserver3.users.responses.LoginResponse
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UsersService(
    val repository: UsersRepository,
    val rolesRepository: RolesRepository,
    val jwt: Jwt ) {

    fun save(req: UserRequest): User {
        val user = User(
            email = req.email!!,
            password = req.password!!
            , name = req.name!!
        )
        val userRole = rolesRepository.findByName("USER")
            ?:throw IllegalStateException("Role 'USER' note Found")

        user.roles.add(userRole)
        log.info("User id={} name={} Added as Role=({})", user.id, user.name, user.roles.joinToString(" | "))
        return repository.save(user)


    }

    fun getById(id: Long) = repository.findById(id)

    /* Solucao utilizando Kotling
    fun findAll(role: String?): List<User>{

        val users = repository.findAll()
        return if (role == null) users
        else users.filter { it.roles.any{r -> r.name == role}}
    }*/
    fun findAll(role: String?): List<User>{

        val sort = Sort.by(Sort.Direction.DESC, "name")
        return if (role == null) repository.findAll(sort)
        else return repository.findAllByRoles(role)
    }

    fun deleteUser(id: Long): Int{


        val user = repository.findByIdOrNull(id)?: return -1

        if (user.roles.any { it.name == "ADMIN" }){
            val count = repository.findAllByRoles(role = "ADMIM").size
            if (count == 1) throw BadRequestException("Cannot delete the last system Admin!")
        }
        log.warn("Usuario deletado id={} name={}", user.id, user.name)
        repository.deleteById(id)
        return 0

    }
    fun login(credentials: LoginRequest): LoginResponse?{

        val user = repository.findUserByEmail(credentials.email!!)?: return null
        if (user.password != credentials.password) return null
        log.info("User logged in id={} name={}", user.id, user.name)
        return LoginResponse(
            token = jwt.createToken(user),
            user.toResponse()
        )
    }

    companion object {

        val log = LoggerFactory.getLogger(UsersService::class.java)


    }

}