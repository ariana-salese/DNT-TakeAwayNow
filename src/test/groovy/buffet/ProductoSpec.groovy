package buffet

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class ProductoSpec extends Specification implements DomainUnitTest<Producto> {

    def setup() {
    }

    def cleanup() {
    }

    void "no puedo crear productos con cantidad menor a cero"() {
        when: "intento instanciar un producto con cantidad menor a cero"
            def alfajor = new Producto("Alfajor", -1, new Dinero(50))

        then: "lanzo una excepción"
            IllegalStateException exception = thrown()
    }

    void "no puedo crear productos con cantidad igual a cero"() {
        when: "intento instanciar un producto con cantidad igual a cero"
            def alfajor = new Producto("Alfajor", 0, new Dinero(50))

        then: "lanzo una excepción"
            IllegalStateException exception = thrown()
    }
}
