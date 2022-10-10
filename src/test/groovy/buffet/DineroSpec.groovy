package buffet

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class DineroSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "tengo dos pesos le sumo 3 pesos y obtengo 5 pesos"() {
        given: "dado dos pesos y tres pesos"
            def dosPesos = new Dinero(2)
            def tresPesos = new Dinero(3)

        when: "al sumarlos"
            def cincoPesos = dosPesos + tresPesos

        then: "obtengo 5 pesos"
            cincoPesos.monto == 5
    }

    void "no puedo tener dinero negativo"() {
        when: "al sumarlos"
            new Dinero(-1)

        then: "obtengo 5 pesos"
            IllegalStateException exception = thrown()
    }
}
