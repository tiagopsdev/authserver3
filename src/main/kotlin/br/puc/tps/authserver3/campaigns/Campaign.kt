package br.puc.tps.authserver3.campaigns

import br.puc.tps.authserver3.users.User
import jakarta.persistence.*
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
    @ManyToOne
    @JoinColumn(name = "master_id")
    var master: User,
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
    var users: MutableSet<User> = mutableSetOf()


    ){

    constructor() : this(null, "", "", User(), "",  0, mutableSetOf())

}
