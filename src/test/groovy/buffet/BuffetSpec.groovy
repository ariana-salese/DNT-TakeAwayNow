package buffet

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class BuffetSpec extends Specification implements DomainUnitTest<Buffet> {

    def setup() {
    }

    def cleanup() {
    }
    
    void "un buffet puede agregar un productos y listar sus respectivos precios"() {
        given: "un buffet que quiere agregar productos a su almacén y listar sus precios"
            def buffet = new Buffet()
            def pancho = new Producto("Pancho")
            def dona = new Producto("Dona")
            def cincoPesos = new Dinero(5)
            def diezPesos = new Dinero(10)

        when: "el buffet registra los productos con su precio y stock"
            buffet.registrarProducto(pancho, diezPesos, 1)
            buffet.registrarProducto(dona, cincoPesos, 5)

        then: "el listado de precios contiene los precios indicados y el almacén los stocks indicados"
            def precios = buffet.getListadoDePrecios()
            precios.size() == 2
            precios["Dona"] == cincoPesos
            precios["Pancho"] == diezPesos
            buffet.hayStock("Dona") == true
            buffet.hayStock("Pancho") == true
            buffet.hayStock("Alfajor") == false
    }

    void "un buffet no puede registrar productos con stock igual a cero"() {
        when: "un buffet registra los productos con un stock igual a cero"
            def buffet = new Buffet()
            def pancho = new Producto("Pancho")
            buffet.registrarProducto(pancho, new Dinero(5), 0)

        then: "se lanza una excepción"
            IllegalStateException exception = thrown()
    }

    void "un buffet no puede registrar productos con stock negativo"() {
        when: "un buffet registra los productos con un stock negativo"
            def buffet = new Buffet()
            def pancho = new Producto("Pancho")
            buffet.registrarProducto(pancho, new Dinero(5), -1)

        then: "se lanza una excepción"
            IllegalStateException exception = thrown()
    }

    void "un buffet no puede registrar productos con precio igual a cero"() {
        when: "un buffet registra los productos con un precio igual a cero"
            def buffet = new Buffet()
            def pancho = new Producto("Pancho")
            buffet.registrarProducto(pancho, new Dinero(0), 10)

        then: "se lanza una excepción"
            IllegalStateException exception = thrown()
    }

    void "un buffet no puede registrar productos con precio negativo"() {
        when: "un buffet registra los productos con un precio negativo"
            def buffet = new Buffet()
            def pancho = new Producto("Pancho")
            buffet.registrarProducto(pancho, new Dinero(-1), 10)

        then: "se lanza una excepción"
            IllegalStateException exception = thrown()
    }
}
