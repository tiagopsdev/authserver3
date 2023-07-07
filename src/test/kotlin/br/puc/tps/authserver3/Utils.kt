package br.puc.tps.authserver3

import br.puc.tps.authserver3.users.Role
import br.puc.tps.authserver3.users.User
import kotlin.random.Random

fun randomString(
    length: Int = 10, allowedChars: List<Char> =
        ('A'..'Z') + ('a'..'z') + ('0'..'9')
) = (1..length)
    .map { allowedChars.random() }
    .joinToString()

fun ramdomQuest(i: Long): String{

    if ((i % 2L) == 0L) {return "Second Quest"}
    if ((i % 3L) == 0L) {return "Third Quest"}
    return "Fourth Quest"

}

