package br.puc.tps.authserver3.users

import jakarta.validation.constraints.Email
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UsersRepository: JpaRepository<User, Long>{

    @Query(value = "select u from User u " +
            "join u.roles r "+
            "where r.name = :role"+
            " order by u.name desc ")
    fun findAllByRoles(role: String): List<User>


    fun findUserByEmail(email: String): User?




}


