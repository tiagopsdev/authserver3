package br.puc.tps.authserver3.campaigns

import br.puc.tps.authserver3.users.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
interface CampaingsRepository: JpaRepository<Campaign, Long>{

    fun findCampaignByTitleContainsIgnoreCase(word: String): MutableList<Campaign>?

    @Query(value = "select c from Campaign c " +
            "join c.systemRule s " +
            "WHERE s.name LIKE CONCAT('%', :sr, '%') " +
            "order by c.maxPlayers desc ")
    fun findAllBySystemRules(sr: String?): List<Campaign>



}

