package br.puc.tps.authserver3.campaigns

import br.puc.tps.authserver3.campaigns.requests.CampaignRequest
import br.puc.tps.authserver3.campaigns.responses.CampaignResponse
import br.puc.tps.authserver3.users.responses.UserResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/campaigns")
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
    fun createCampaign(@RequestBody @Validated reqCamp: CampaignRequest, auth: Authentication) :ResponseEntity<CampaignResponse> {

        var userid = auth.credentials as Long
        return service.save(reqCamp, userid).toResponse()
            .let {
                ResponseEntity.status(CREATED).body(it)
            }

    }





    @GetMapping("/{id}")
    fun getCampaign(@PathVariable("id") id: Long) =
        service.getById(id).orElse(null)
            ?.let { ResponseEntity.ok(it.toResponse())}
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("permitAll()")
    @SecurityRequirement(name = "authserver3")
    fun deleteCampaign(@PathVariable("id") id: Long, auth: Authentication): ResponseEntity<out Any> {

        return if (service.deletecampaingById(id, auth.credentials as Long)) {
            ResponseEntity.ok().build()
        }else
            { ResponseEntity.notFound().build()}



    }
    @PutMapping("/user")
    @PreAuthorize("permitAll()")
    @SecurityRequirement(name = "authserver3")
    fun updateUserCampaign(@RequestParam("idUser") idUser: Long,
                           @RequestParam("idCampaing") idCampaing: Long,
                           @RequestParam("action") action: String,
                    auth: Authentication): ResponseEntity<Any> {

        return if (service.updateUserCampaign(idCampaing, idUser, action, auth.credentials as Long))
        {
            ResponseEntity.ok().build()
        }else{
            ResponseEntity.notFound().build()
        }
    }



    private fun Campaign.toResponse(): CampaignResponse{

        var usersResplist = mutableListOf<UserResponse>()
        users.forEach { user -> usersResplist.add(user.toResponse()) }

        return CampaignResponse(id!!, title, systemRule.name, master.name, maxPlayers, users.size.toLong(), usersResplist.toList())
    }






}