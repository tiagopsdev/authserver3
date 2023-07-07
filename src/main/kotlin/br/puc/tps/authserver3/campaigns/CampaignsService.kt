package br.puc.tps.authserver3.campaigns

import br.puc.tps.authserver3.campaigns.requests.CampaignRequest
import br.puc.tps.authserver3.exception.*
import br.puc.tps.authserver3.systemrules.SystemRulesRepository
import br.puc.tps.authserver3.users.UsersRepository
import br.puc.tps.authserver3.users.UsersService
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CampaignsService(
    val campaingsRepository: CampaingsRepository,
    val usersRepository: UsersRepository,
    val srRepository: SystemRulesRepository
) {

    companion object {

        val log = LoggerFactory.getLogger(CampaignsService::class.java)


    }
    fun save(req: CampaignRequest, userid: Long): Campaign {

        var user = usersRepository.findByIdOrNull(userid)
            ?: throw MUnespectNullException("Should the logged user been found here")
        var master = user
        var systemRule = srRepository.findSystemRuleByName(req.systemRule!!)
            ?: throw BadRequestException(message = "System Rule not found")
        var cp = Campaign(
            title = req.title!!,
            systemRule = systemRule,
            master = master,
            password = req.password!!,
            maxPlayers = req.maxPlayers!!,
        )
        cp.users.add(user)
        log.info("User id={} name={} master={}", cp.id, cp.title, cp.master.name)

        return campaingsRepository.save(cp)
    }

    fun getById(id: Long) = campaingsRepository.findById(id)

    fun findAll(): List<Campaign> {

        val cps = campaingsRepository.findAll()
        return cps

    }

    fun getByTitleContains(str: String?): List<Campaign>? {

        if (str == null || str.isEmpty()) return campaingsRepository.findAll()
        val words: List<String> = str.split("\n")
        val campaigns: MutableList<Campaign> = mutableListOf()
        for (w in words) {
            campaingsRepository.findCampaignByTitleContainsIgnoreCase(w)?.let { campaigns.addAll(it) }
        }
        return campaigns.toList()


    }

    fun findAllBySystemRules(sr: String?): List<Campaign> {
        var campaignList =  campaingsRepository.findAllBySystemRules(sr)
        if (campaignList.isEmpty()) throw NotFoundException("Campaign", "Not found Campaing with System Rules ${sr}")
        return campaignList
    }

    fun deletecampaingById(id: Long, loggedUserID: Long): Boolean {

        var campaign =
            campaingsRepository.findByIdOrNull(id)
                ?: throw NotFoundException("Campaign", "${id}", "Not found Campaing")
        if (campaign.master.id != loggedUserID)
            throw UnauthorizedRequestException(message = "Not the owner")
        if (campaign.users.size > 1)
            throw ForbiddenException("The Capaign ${campaign.title} has more then one player and Don't, Left ${campaign.users.size - 1} to leave this campaigns")
        campaingsRepository.deleteById(id)
        log.warn("Campaign {} deleted!", campaign.id)
        return true

    }

    fun updateUserCampaign(idCampaign: Long, idUser: Long, action: String, loggedUserID: Long): Boolean {

        var campaign = campaingsRepository.findByIdOrNull(idCampaign)
            ?: throw NotFoundException("Campaign", "${idCampaign}", "Not found Campaing")
        if (campaign.master.id != loggedUserID) throw UnauthorizedRequestException(message = "Not the owner")
        val user =
            usersRepository.findByIdOrNull(idUser) ?: throw NotFoundException("User", "${idUser}", "Not found User")
        if (action == "add") {

            if (campaign.users.contains(user)) return true
            campaign.users.add(user)
            campaingsRepository.save(campaign)
            return true //@Todo("Execptions, Reduzir número de chamadas para o banco")
        } else if (action == "remove") {

            if (!(campaign.users.contains(user))) throw NotFoundException(
                "Campaign",
                "${idUser}",
                "Not found User in a Campaing"
            )
            campaign.users.remove(user)
            campaingsRepository.save(campaign)
            log.info("Action - {}, User {}, Campaing {}", action, idUser, idCampaign)
            //return true //@Todo("Execptions, Reduzir número de chamadas para o banco")

        }


        throw BadRequestException("$action is not reckonized as a valid parameter")
    }


}