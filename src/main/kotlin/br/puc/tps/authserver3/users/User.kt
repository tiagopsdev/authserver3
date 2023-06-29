package br.puc.tps.authserver3.users

import br.puc.tps.authserver3.campaigns.Campaign
import br.puc.tps.authserver3.users.responses.UserResponse
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name ="TblUser")
class User (

    @Id @GeneratedValue
    var id: Long? = null,
    @Email
    @Column( unique = true, nullable = false)
    var email: String,
    @Column(length = 50)
    var password: String,

    @Column(nullable = false)
    var name: String = "",

    @ManyToMany
    @JoinTable(

        name = "UserRole",
        joinColumns = [JoinColumn(name="idUser")],
        inverseJoinColumns = [JoinColumn(name="idRole")]

    )
    val roles: MutableSet<Role> = mutableSetOf(),

    @ManyToMany(mappedBy = "users")
    val campaigns: MutableSet<Campaign> = mutableSetOf()




    ) {
        constructor() : this(null, "", "", "", mutableSetOf())

        fun toResponse() = UserResponse(id!!, name, email)
}