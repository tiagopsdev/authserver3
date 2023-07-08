package br.puc.tps.authserver3.users

import br.puc.tps.authserver3.UserStubs.userStub
import br.puc.tps.authserver3.exception.BadRequestException
import br.puc.tps.authserver3.security.Jwt
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

internal class UsersServiceTest {
    private val usersRepositoryMock = mockk<UsersRepository>()
    private val rolesRepositoryMock = mockk<RolesRepository>()
    private val jwtMock = mockk<Jwt>()

    private val service = UsersService(usersRepositoryMock, rolesRepositoryMock, jwtMock)

    @Test
    fun `Delete should return -1 If the user does not exists`() {
        every { usersRepositoryMock.findByIdOrNull(1) } returns null
        service.deleteUser(1) shouldBe -1
    }

    @Test
    fun `Delete must return 0 if the user is deleted`() {
        val user = userStub(1)
        every { usersRepositoryMock.findByIdOrNull(1) } returns user
        justRun { usersRepositoryMock.deleteById(user.id!!) }
        service.deleteUser(1) shouldBe 0
    }

    /*
    fun `Delete should throw a BadRequestException if the user is the last admin`() {
        every { usersRepositoryMock.findByIdOrNull(1) } returns userStub(roles = listOf("ADMIN"))
        every {
            usersRepositoryMock.findAllByRoles(role = "ADMIN")
        } answers { listOf(userStub(roles = listOf("ADMIN")))}

        shouldThrow<BadRequestException> {
            service.deleteUser(1)
        } shouldHaveMessage "Cannot delete the last system admin!"
    }*/




}