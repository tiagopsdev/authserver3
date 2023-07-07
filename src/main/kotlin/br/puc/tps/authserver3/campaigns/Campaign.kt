package br.puc.tps.authserver3.campaigns

import br.puc.tps.authserver3.systemrules.SystemRule
import br.puc.tps.authserver3.users.User
import jakarta.persistence.*

@Entity
@Table(name = "tblCampaign")
class Campaign(

    @Id @GeneratedValue
    var id: Long? = null,

    @Column(nullable = false)
    var title: String,
    @ManyToOne
    @JoinColumn(
        name = "systemrules_id",
        nullable = false)
    var systemRule: SystemRule,
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

    constructor() : this(null, "", SystemRule(), User(), "",  0, mutableSetOf())

}
