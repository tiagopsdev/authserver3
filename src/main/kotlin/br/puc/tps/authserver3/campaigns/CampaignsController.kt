package br.puc.tps.authserver3.campaigns

import br.puc.tps.authserver3.campaigns.requests.CampaignRequest
import br.puc.tps.authserver3.campaigns.responses.CampaignResponse
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/campaings")
class CampaignsController(val service: CampaignsService) {

    @GetMapping
    fun listCampaigns() = service.findAll().map { it.toResponse() }

    @GetMapping("/byTitle")
    fun listCampaignsByTitles(@RequestParam("title") title: String?) = service.getByTitleContains(title)?.map { it.toResponse() }

    @GetMapping("/bySystemRules")
    fun listCampaignsbySystemRules(@RequestParam("systemRule") systemRule: String?) = service.findAllBySystemRules(systemRule)?.map { it.toResponse() }

    @Transactional
    @PostMapping
    @PreAuthorize("permitAll()")
    fun createCampaign(@RequestBody @Validated reqCamp: CampaignRequest) =

        service.save(
            Campaign(
                title = reqCamp.title!!,
                systemRules = reqCamp.systemRules!!,
                master = "GM",
                password = reqCamp.password!!,
                maxPlayers = reqCamp.maxPlayers!!
            )
        ).toResponse()
            .let {
                ResponseEntity.status(CREATED).body(it)
            }



    @GetMapping("/{id}")
    fun getCampaign(@PathVariable("id") id: Long) =
        service.getById(id).orElse(null)
            ?.let { ResponseEntity.ok(it.toResponse())}
            ?: ResponseEntity.notFound().build()










    private fun Campaign.toResponse() = CampaignResponse(id!!, title, systemRules, maxPlayers)




}