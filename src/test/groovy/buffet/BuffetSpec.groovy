package buffet

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class BuffetSpec extends Specification implements DomainUnitTest<Buffet> {

    def setup() {
    }

    def cleanup() {
    }

    void "un buffet puede agregar una serie de productos"() {
        given: "un buffet que quiere agregar productos"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 1, precioPancho)
            def precioDona = new Dinero(5)
            def dona = new Producto("dona", 5, precioDona)

        when: "el buffet registra los productos"
            buffet.registrarProducto(pancho)
            buffet.registrarProducto(dona)

        then: "hay stock del productos registrados y sus precios don correctos"
            def inventario = buffet.almacen.inventario
            //chequea precio
            inventario["dona"].precio == precioDona
            inventario["pancho"].precio == precioPancho
            //chequea stock
            buffet.hayStock("dona") == true
            buffet.hayStock("pancho") == true
            inventario.size() == 2
    }

    void "un buffet puede actualizar el precio de un producto"() {

    }

    void "un buffet no puede actualizar el precio de un producto como menor a cero"() {

    }

    void "un buffet no puede actualizar el precio de un producto como igual a cero"() {

    }

    void "un buffet no puede actualizar el precio de un producto que no tiene"() {
        
    }

    void "un buffet puede ingresar nuevo stock de un producto"() {

    }

    void "un buffet no puede ingresar nuevo stock de un producto que no tiene"() {
        
    }

    void "un buffet no puede ingresar stock de un producto que no tiene"() {
        
    }

    void "un buffet no puede ingresar stock negativo de un producto"() {
        
    }

    void "un buffet no puede ingresar stock igual a cero de un producto"() {
        
    }

    void "un buffet puede agregar un producto a un pedido si hay stock"() {

    }

    void "un buffet no puede agregar un producto a un pedido si no hay suficiente stock"() {

    }

    void "un buffet no puede agregar un producto a un pedido si no lo tiene"() {

    }
}
