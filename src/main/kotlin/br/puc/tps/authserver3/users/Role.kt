package br.puc.tps.authserver3.users

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany

@Entity
class Role (

    @Id @GeneratedValue
    val id: Long? = null,
    @Column(unique = true, nullable = false)
    val name: String,
    @ManyToMany(mappedBy = "roles")
    val users: MutableSet<User> = mutableSetOf()

) {
    constructor() : this(null, "") {

    }
}