package br.puc.tps.authserver3.systemrules

import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.ContextStartedEvent
import org.springframework.stereotype.Component

@Component
class SystemRulesBootstrap(
    val srRepository: SystemRulesRepository,

):
    ApplicationListener<ContextRefreshedEvent>{
    override fun onApplicationEvent(event: ContextRefreshedEvent) {


        if (srRepository.count() == 0L) {
            //DND
            srRepository.save(SystemRule(name = "DND"))
            //DAEMON
            srRepository.save(SystemRule(name = "DAEMON"))
            //SavageWorlds
            srRepository.save(SystemRule(name = "Savage Worlds"))

        }

    }

}

