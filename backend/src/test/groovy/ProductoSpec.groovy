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
            Exception e = thrown()
            e.message == "No se puede inicializar un producto con cantidad igual a cero."
    }

    void "no puedo crear productos con cantidad igual a cero"() {
        when: "intento instanciar un producto con cantidad igual a cero"
            def alfajor = new Producto("Alfajor", 0, new Dinero(50))

        then: "lanzo una excepción"
            Exception e = thrown()
            e.message == "No se puede inicializar un producto con cantidad igual a cero."
    }

    void "no puedo crear productos con precio igual a cero"() {
        when: "intento instanciar un producto con precio igual a cero"
            def alfajor = new Producto("Alfajor", 1, new Dinero(0))

        then: "lanzo una excepción"
            Exception e = thrown()
            e.message == "No se puede inicializar un producto con precio menor o igual a cero."
    }

    void "no puedo crear productos con precio negativo"() {
        when: "intento instanciar un producto con precio negativo"
            def alfajor = new Producto("Alfajor", 1, new Dinero(-10))

        then: "lanzo una excepción"
            Exception e = thrown()
            e.message == "No puede existir dinero negativo."
    }
}
