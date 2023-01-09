package takeawaynow

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class PuntosDeConfianzaSpec extends Specification implements DomainUnitTest<PuntosDeConfianza> {

    def setup() {
    }

    def cleanup() {
    }

    void "crear puntos de confianza con cantidad negativo lanza error"() {
        when: "se crean puntos con cantidad negativa"
            PuntosDeConfianza puntos = new PuntosDeConfianza(-100)

        then: "se lanza error"
            IllegalStateException exception = thrown()
    }

    void "sumar puntos con cantidad 100 y puntos con cantidad 50 el resultado es 150"() {
        given: "dos puntos de confianza con cantidad 100 y 50"
            PuntosDeConfianza puntos100 = new PuntosDeConfianza(100)
            PuntosDeConfianza puntos50 = new PuntosDeConfianza(50)

        when: "al sumarlos"
            PuntosDeConfianza puntos150 = puntos100 + puntos50

        then: "la suma da puntos con cantidad 150"
            puntos150.cantidad == 150
    }

    void "restar puntos con cantidad 100 y puntos con cantidad 50 el resultado es 50"() {
        given: "dos puntos de confianza con cantidad 100 y 50"
            PuntosDeConfianza puntos100 = new PuntosDeConfianza(100)
            PuntosDeConfianza puntos50 = new PuntosDeConfianza(50)

        when: "al restarloe"
            PuntosDeConfianza puntos150 = puntos100 - puntos50

        then: "la suma da puntos con cantidad 50"
            puntos150.cantidad == 50
    }

    void "sumar puntos con cantidad 100 y puntos con cantidad 50 el resultado es 150"() {
        given: "dos puntos de confianza con cantidad 100 y 50"
            PuntosDeConfianza puntos100 = new PuntosDeConfianza(100)

        when: "al sumarlos"
            PuntosDeConfianza puntos150 = puntos100 + 50

        then: "la suma da puntos con cantidad 150"
            puntos150.cantidad == 150
    }

    //TODO testear cuando queda menor a 0
}
