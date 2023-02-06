package takeawaynow

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

    void "tengo dos pesos, lo multiplico por cinco y obtengo diez pesos"() {
        given: "dos pesos"
            def dosPesos = new Dinero(2)

        when: "lo multiplico por cinco"
            def diezPesos = dosPesos * 5

        then: "obtengo diez pesos"
            diezPesos.monto == 10
    }

    void "no puedo multiplicar dinero por cero"() {
        given: "dos pesos"
            def dosPesos = new Dinero(2)

        when: "lo multiplico por cero"
            def ceroPesos = dosPesos * 0

        then: "obtengo cero pesos"
            IllegalStateException exception = thrown()
    }

    void "no puedo multiplicar dinero por un número negativo"() {
        given: "dos pesos"
            def dosPesos = new Dinero(2)

        when: "lo multiplico por menos uno"
            def menosDosPesos = dosPesos * -1

        then: "obtengo menos dos pesos"
            IllegalStateException exception = thrown()
    }

    void "no puedo tener dinero negativo"() {
        given: "dado dos pesos y tres pesos"
            def dosPesos = new Dinero(2)
            def tresPesos = new Dinero(3)

        when: "al restarlos"
            def resultado = dosPesos - tresPesos

        then: "lanza exepcion"
            IllegalStateException exception = thrown()
    }
}
