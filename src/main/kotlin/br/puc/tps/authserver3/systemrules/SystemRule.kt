package br.puc.tps.authserver3.systemrules

import br.puc.tps.authserver3.campaigns.Campaign
import jakarta.persistence.*

@Entity
class SystemRule (

    @Id @GeneratedValue
    val id: Long? = null,
    @Column(unique = true, nullable = false)
    val name: String,
    @OneToMany(mappedBy = "systemRule")
    val campaigns: MutableSet<Campaign> = mutableSetOf()

) {
    constructor() : this(null, "") {

    }
}