package br.puc.tps.authserver3.campaigns

import br.puc.tps.authserver3.campaigns.requests.CampaignRequest
import br.puc.tps.authserver3.exception.MUnespectNullException
import br.puc.tps.authserver3.users.UsersRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CampaignsService(
    val campaingsRepository: CampaingsRepository,
    val usersRepository: UsersRepository
) {

    fun save(req: CampaignRequest, userid: Long): Campaign{

        var user = usersRepository.findByIdOrNull(userid) ?: throw MUnespectNullException("Should the logged user been found here")
        var master = user
        var cp = Campaign(
            title = req.title!!,
            systemRules = req.systemRules!!,
            master = master,
            password = req.password!!,
            maxPlayers = req.maxPlayers!!,
        )
        cp.users.add(user)

        return campaingsRepository.save(cp)
    }

    fun getById(id: Long) = campaingsRepository.findById(id)

    fun findAll(): List<Campaign> {

        val cps = campaingsRepository.findAll()
        return cps

    }

    fun getByTitleContains(str: String?): List<Campaign>? {

        if (str == null || str.isEmpty())  return campaingsRepository.findAll()
        val words: List<String> = str.split("\n")
        val campaigns: MutableList<Campaign> = mutableListOf()
        for (w in words){
            campaingsRepository.findCampaignByTitleContainsIgnoreCase(w)?.let { campaigns.addAll(it)}
        }
        return campaigns.toList()


    }

    fun findAllBySystemRules(sr: String?) = campaingsRepository.findAllBySystemRules(sr)

    fun deletecampaingById(id: Long, loggedUserID: Long): Boolean {

        var campaign = campaingsRepository.findByIdOrNull(id) ?: return false //Not Found
        if (campaign.master.id != loggedUserID) return false //Not the Owner
        if (campaign.users.size > 1) return false // Not Empty
        campaingsRepository.deleteById(id)
        return true //@Todo("Execptions")


    }

    fun updateUserCampaign(idCampaign: Long, idUser: Long, action: String, loggedUserID: Long): Boolean {

        var campaign = campaingsRepository.findByIdOrNull(idCampaign) ?: return false //Campaign Not Found
        if (campaign.master.id != loggedUserID) return false //Not the Owner
        val user = usersRepository.findByIdOrNull(idUser) ?: return false //User not Found
        if (action == "add") {

            if (campaign.users.contains(user)) return true //User already inserted
            campaign.users.add(user)
            campaingsRepository.save(campaign)
            return true //@Todo("Execptions, Reduzir número de chamadas para o banco")
        }else if (action == "remove"){

            if (!(campaign.users.contains(user))) return true //User not find in campaign
            campaign.users.remove(user)
            campaingsRepository.save(campaign)
            return true //@Todo("Execptions, Reduzir número de chamadas para o banco")

        }


        return TODO("Provide the return value")
    }




}