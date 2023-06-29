package br.puc.tps.authserver3.campaigns

import org.springframework.stereotype.Service

@Service
class CampaignsService(val repository: CampaingsRepository) {

    fun save(campaign: Campaign) = repository.save(campaign)

    fun getById(id: Long) = repository.findById(id)

    fun findAll(): List<Campaign> = repository.findAll()

    fun getByTitleContains(str: String?): List<Campaign>? {

        if (str == null || str.isEmpty())  return repository.findAll()
        val words: List<String> = str.split("\n")
        val campaigns: MutableList<Campaign> = mutableListOf()
        for (w in words){
            repository.findCampaignByTitleContainsIgnoreCase(w)?.let { campaigns.addAll(it)}
        }
        return campaigns.toList()


    }

    fun findAllBySystemRules(sr: String?) = repository.findAllBySystemRules(sr)


}