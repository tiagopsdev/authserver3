package br.puc.tps.authserver3.systemrules

import org.springframework.data.jpa.repository.JpaRepository

interface SystemRulesRepository: JpaRepository<SystemRule, Long>{

    fun findSystemRuleByName(srName: String): SystemRule?

}