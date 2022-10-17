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
        given: "un buffet que quiere actualizar el precio de un producto"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 1, precioPancho)

        
        when: "el buffet actualiza el precio"
            buffet.registrarProducto(pancho)
            def nuevoPrecioPancho = new Dinero(15)
            buffet.actualizarPrecio("pancho", nuevoPrecioPancho)

        then: "el precio fue actualizado"
            buffet.almacen.inventario["pancho"].precio == nuevoPrecioPancho
    }

    void "un buffet no puede actualizar el precio de un producto como igual a cero"() {
        given: "un buffet que quiere actualizar el precio de un producto"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 1, precioPancho)

        
        when: "el buffet actualiza el precio como cero"
            buffet.registrarProducto(pancho)
            def nuevoPrecioPancho = new Dinero(0)
            buffet.actualizarPrecio("pancho", nuevoPrecioPancho)

        then: "el precio no fue actualizado y se lanzo error"
            IllegalStateException exception = thrown()
            buffet.almacen.inventario["pancho"].precio == precioPancho
    }

    void "un buffet no puede actualizar el precio de un producto que no tiene"() {
        given: "un buffet que quiere actualizar el precio de un producto"
            def buffet = new Buffet()

        
        when: "el buffet actualiza el precio que no tiene"
            buffet.actualizarPrecio("pancho", new Dinero(15))

        then: "se lanza error"
            IllegalStateException exception = thrown()
    }

    void "un buffet puede ingresar nuevo stock de un producto"() {
        given: "un buffet que quiere actualizar el stock de un producto"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 10, precioPancho)

        
        when: "el buffet actualiza el stock"
            buffet.registrarProducto(pancho)
            buffet.ingresarStock("pancho", 5)

        then: "el stock se incremento"
            buffet.almacen.inventario["pancho"].cantidad == 15
    }

    void "un buffet no puede ingresar nuevo stock de un producto que no tiene"() {
        given: "un buffet que quiere actualizar el stock de un producto"
            def buffet = new Buffet()
        
        when: "el buffet ingresa stock de un producto que no tiene"
            buffet.ingresarStock("pancho", 5)

        then: "se lanzo error"
            IllegalStateException exception = thrown()
    }

    void "un buffet no puede ingresar stock negativo de un producto"() {
        given: "un buffet que quiere actualizar el stock de un producto"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 10, precioPancho)

        
        when: "el buffet ingresa stock negativo"
            buffet.registrarProducto(pancho)
            buffet.ingresarStock("pancho", -1)

        then: "se lanzo un error"
            IllegalStateException exception = thrown()
    }

    void "un buffet no puede ingresar stock igual a cero de un producto"() {
        given: "un buffet que quiere actualizar el stock de un producto"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 10, precioPancho)

        when: "el buffet ingresa stock cero del producto"
            buffet.registrarProducto(pancho)
            buffet.ingresarStock("pancho", 0)

        then: "se lanza error"
            IllegalStateException exception = thrown()
    }

    void "un buffet puede agregar un producto a un pedido si hay stock"() {
        given: "un buffet que quiere agregar un producto a un pedido"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 10, precioPancho)
            def pedido = new Pedido()

        when: "el buffet agrega el producto al pedido"
            buffet.registrarProducto(pancho)
            buffet.agregarAlPedido("pancho", 2, pedido)

        then: "el pedido tiene el producto y el stock se actualizo"
            pedido.cantidadDeProductos() == 2
            buffet.almacen.inventario["pancho"].cantidad == 8
    }

    void "un buffet no puede agregar un producto a un pedido si no hay suficiente stock"() {
        given: "un buffet que quiere agregar un producto a un pedido"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 1, precioPancho)
            def pedido = new Pedido()

        when: "el buffet intenta agregar un producto sin stock al pedido"
            buffet.registrarProducto(pancho)
            buffet.agregarAlPedido("pancho", 2, pedido)

        then: "se lanza error"
            IllegalStateException exception = thrown()
    }

    void "un buffet no puede agregar un producto a un pedido si no lo tiene"() {
        given: "un buffet que quiere agregar un producto a un pedido"
            def buffet = new Buffet()
            def pedido = new Pedido()

        when: "el buffet intenta agregar un producto que no tiene al pedido"
            buffet.agregarAlPedido("pancho", 2, pedido)

        then: "se lanza error"
            IllegalStateException exception = thrown()
    }
}
