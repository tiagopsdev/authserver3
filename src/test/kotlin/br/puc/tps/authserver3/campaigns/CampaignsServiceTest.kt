package br.puc.tps.authserver3.campaigns

import br.puc.tps.authserver3.CampaignStubs.campaignStub
import br.puc.tps.authserver3.UserStubs.userStub
import br.puc.tps.authserver3.exception.BadRequestException
import br.puc.tps.authserver3.exception.ForbiddenException
import br.puc.tps.authserver3.exception.NotFoundException
import br.puc.tps.authserver3.exception.UnauthorizedRequestException
import br.puc.tps.authserver3.systemrules.SystemRulesRepository
import br.puc.tps.authserver3.users.UsersRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull

class CampaignsServiceTest {

    private val campaingsRepositoryMock = mockk<CampaingsRepository>()
    private val usersRepositoryMock = mockk<UsersRepository>()
    private val srRepositoryMock = mockk<SystemRulesRepository>()

    private val service = CampaignsService(campaingsRepositoryMock, usersRepositoryMock, srRepositoryMock)

    @Test
    fun `Should throw exeption if campaign wasn't found`() {

        every { campaingsRepositoryMock.findByIdOrNull(1) } returns null
        assertThrows<NotFoundException> {

            service.deletecampaingById(1L, 1L)

        }

    }

    @Test
    fun `Should throw UnauthorizedRequestException if is not the owner`() {

        every { campaingsRepositoryMock.findByIdOrNull(1) } returns campaignStub()
        assertThrows<UnauthorizedRequestException> {

            service.deletecampaingById(1L, 1001L)

        }

    }
    @Test
    fun `Should throw ForbiddenException if there are more then one user in this Campaign`() {

        every { campaingsRepositoryMock.findByIdOrNull(1L) } returns campaignStub(id=1L, qtdusers = 2)
        assertThrows<ForbiddenException> {

            service.deletecampaingById(1L, 1L)

        }

    }
    @Test
    fun `Should delete the campaign`() {

        var campaign = campaignStub(id=1L, qtdusers = 1)
        every { campaingsRepositoryMock.findByIdOrNull(1L) } returns campaign
        justRun { campaingsRepositoryMock.deleteById(1L) }
        service.deletecampaingById(1L, 1L) shouldBe true
    }

    @Test
    fun `Should throw exception if campaign wasn't found before add or rem user`() {

        every { campaingsRepositoryMock.findByIdOrNull(1) } returns null
        assertThrows<NotFoundException> {

            service.updateUserCampaign(1L, 1L, loggedUserID = 1L, action = "add")


        }
        assertThrows<NotFoundException> {


            service.updateUserCampaign(1L, 1L, loggedUserID = 1L, action = "remove")

        }

    }
    @Test
    fun `Should throw UnauthorizedRequestException if a tenant`() {

        every { campaingsRepositoryMock.findByIdOrNull(1) } returns campaignStub()
        assertThrows<UnauthorizedRequestException> {

            service.updateUserCampaign(1L, 2L, loggedUserID = 1001L, action = "add")

        }
        assertThrows<UnauthorizedRequestException> {

            service.updateUserCampaign(1L, 1L, loggedUserID = 1001L, action = "remove")

        }

    }
    @Test
    fun `Should throw Exception if not found  user submitted`() {


        every { campaingsRepositoryMock.findByIdOrNull(1) } returns campaignStub(id=1L)
        every { usersRepositoryMock.findByIdOrNull(2) } returns null

        assertThrows<NotFoundException> {

            service.updateUserCampaign(1L, 2L, loggedUserID = 1L, action = "add")


        }
        assertThrows<NotFoundException> {

            service.updateUserCampaign(1L, 2L, loggedUserID = 1L, action = "remove")

        }

    }
    @Test
    fun `Should throw Exception if the action parameter is not Right`() {

        var campaign = campaignStub(id=1L)
        every { campaingsRepositoryMock.findByIdOrNull(1) } returns campaignStub(id=1L)
        every { usersRepositoryMock.findByIdOrNull(2) } returns userStub(2L)

        assertThrows<BadRequestException> {

            service.updateUserCampaign(1L, 2L, loggedUserID = 1L, action = "InvalidParameter")


        }

    }
    @Test
    fun `Should throw exception if user was not found in campaign`() {

        var cp = campaignStub(id=1L)
        var user = userStub(10)
        every { campaingsRepositoryMock.findByIdOrNull(1) } returns cp
        every { usersRepositoryMock.findByIdOrNull(10) } returns user

        assertThrows<NotFoundException> {

            service.updateUserCampaign(idCampaign = 1L, idUser = 10L, action = "remove", loggedUserID = 1L) shouldBe true

        }

    }

    @Test
    fun `Should add user`() {

        var cp = campaignStub(id = 1L)
        every { campaingsRepositoryMock.findByIdOrNull(1) } returns cp
        every { usersRepositoryMock.findByIdOrNull(2) } returns userStub(2L)

        every { campaingsRepositoryMock.save(cp) } returns cp
        service.updateUserCampaign(idCampaign = 1L, idUser = 2L, action = "add", loggedUserID = 1L) shouldBe true

    }

    @Test
    fun `Should remove user`() {

        var cp = campaignStub(id=1L)
        var user = cp.users.elementAt(0)
        every { campaingsRepositoryMock.findByIdOrNull(1) } returns cp
        every { usersRepositoryMock.findByIdOrNull(2) } returns user

        every { campaingsRepositoryMock.save(cp)} returns cp

        service.updateUserCampaign(idCampaign = 1L, idUser = 2L, action = "remove", loggedUserID = 1L) shouldBe true


    }
    /*fun updateUserCampaign(idCampaign: Long, idUser: Long, action: String, loggedUserID: Long): Boolean {

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


        return TODO("Provide the return value")
    }*/
}