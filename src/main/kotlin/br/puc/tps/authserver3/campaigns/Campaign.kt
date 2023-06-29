package br.puc.tps.authserver3.campaigns

import br.puc.tps.authserver3.users.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "tblCampaign")
class Campaign(

    @Id @GeneratedValue
    var id: Long? = null,

    @Column(nullable = false)
    var title: String,
    @Column(nullable = false)
    var systemRules: String,
    @Column(nullable = false)
    var master: String,
    @Column(length = 48)
    var password: String,
    @Column(nullable = false)
    var maxPlayers: Long,

    @ManyToMany
    @JoinTable(

        name = "CampaignUser",
        joinColumns = [JoinColumn(name="idCampaign")],
        inverseJoinColumns = [JoinColumn(name="idUser")]

    )
    var Users: MutableSet<User> = mutableSetOf()


    )
