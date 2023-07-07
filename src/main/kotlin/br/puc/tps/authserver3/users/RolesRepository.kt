package br.puc.tps.authserver3.users

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RolesRepository: JpaRepository <Role, Long>{

    fun findByName(name: String): Role?

}