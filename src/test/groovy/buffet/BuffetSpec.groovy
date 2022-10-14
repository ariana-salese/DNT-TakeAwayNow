package buffet

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class BuffetSpec extends Specification implements DomainUnitTest<Buffet> {

    def setup() {
    }

    def cleanup() {
    }
    
    // void "un buffet puede agregar un productos al almacen"() {
    //     given: "un cliente que quiere un producto con stock"
    //         def buffet = new Buffet()
    //         def pancho = new Producto("Pancho")
    //         def dona = new Producto("Dona")

    //     when: "el cliente agrega el producto al carrito"
    //         buffet.registrarProducto(pancho, 10, 1)
    //         buffet.registrarProducto(dona, 5, 2)

    //     then: "el carrito tiene un producto"
    //         buffet.getListadoDePrecios()["Dona"] == 5
    //         buffet.getListadoDePrecios()["Pancho"] == 10
    // }
}
