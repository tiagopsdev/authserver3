package br.puc.tps.authserver3.users

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RolesRepository: JpaRepository <Role, Long>{

    fun findByName(name: String): Role?

}