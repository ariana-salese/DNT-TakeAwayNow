package takeawaynow

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class PuntosDeConfianzaSpec extends Specification {

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
    // ??? -> preguntarle a Ari porque pasaria esto

    //TODO testear cuando queda menor a 0
    void "restar puntos con cantidad 100 y puntos con cantidad 150 el resultado es 0"() {
        given: "dos puntos de confianza con cantidad 100 y 50"
            PuntosDeConfianza puntos100 = new PuntosDeConfianza(100)
            PuntosDeConfianza puntos150 = new PuntosDeConfianza(150)


        when: "al restarlos"
            PuntosDeConfianza puntosNegativos = puntos100 - puntos150

        then: "se lanza una excepcion"
            IllegalStateException exception = thrown()
            exception.message == "No hay puntos de confianza suficientes para canjear."
    }
}
