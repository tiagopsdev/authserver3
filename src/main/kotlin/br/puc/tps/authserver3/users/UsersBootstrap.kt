package br.puc.tps.authserver3.users

import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.ContextStartedEvent
import org.springframework.stereotype.Component

@Component
class UsersBootstrap(
    val rolerepository: RolesRepository,
    val userrepository: UsersRepository
):
    ApplicationListener<ContextRefreshedEvent>{
    override fun onApplicationEvent(event: ContextRefreshedEvent) {

        val adminRole = Role(name = "ADMIN")
        if (rolerepository.count() == 0L){
            rolerepository.save(adminRole)
            rolerepository.save(Role(name = "USER"))

        }
        if (userrepository.count() == 0L){

            val adminUser = User(
                email = "gamemaster@google.com",
                password = "gam3m@st3r0",
                name = "Game Master")
            adminUser.roles.add(adminRole)
            userrepository.save(adminUser)

        }

    }

}

