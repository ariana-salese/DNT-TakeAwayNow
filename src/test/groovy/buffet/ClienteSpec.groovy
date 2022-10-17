package buffet

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class ClienteSpec extends Specification{

    def setup() {
    }

    def cleanup() {
    }

    void "un cliente puede agregar un producto con stock al carrito"() {
        given: "un cliente que quiere un producto con stock"
            def cliente = new Cliente()
            def buffet = new Buffet()
            def pancho = new Producto("pancho", 1, new Dinero(10))

        when: "el cliente agrega el producto al carrito"
            buffet.registrarProducto(pancho)
            cliente.ingresarBuffet(buffet)
            cliente.agregarAlPedido("pancho", 1)

        then: "el carrito tiene un producto"
            cliente.cantidadDeProductos() == 1
    }

    void "un cliente no puede agregar un producto sin stock sufuciente al carrito"() {
       given: "un cliente que quiere un producto sin stock"
            def cliente = new Cliente()
            def buffet = new Buffet()
            def pancho = new Producto("pancho", 1, new Dinero(10))
    
       when: "el cliente intenta agrega el producto sin stock al carrito"
            buffet.registrarProducto(pancho)
            cliente.ingresarBuffet(buffet)
            cliente.agregarAlPedido("pancho", 2)

       then: "el carrito no tiene productos"
            IllegalStateException exception = thrown()
            cliente.pedido.cantidadDeProductos() == 0
    }

    void "dado un cliente que tiene 10 pesos de saldo y compro un pancho de 5 pesos me terminan quedando de saldo 5 pesos"() {
        given: "dado un cliente que tiene 10 pesos de saldo y un producto pancho de 5 pesos"
            def cliente = new Cliente()
            cliente.cargarSaldo(new Dinero(10))
            def buffet = new Buffet()
            def pancho = new Producto("pancho", 10, new Dinero(5))

        when: "el cliente compra el pancho"
            buffet.registrarProducto(pancho)
            cliente.ingresarBuffet(buffet)
            cliente.agregarAlPedido("pancho", 1)
            cliente.comprar()

        then: "el saldo del cliente es de 5 pesos"
            cliente.saldo == new Dinero(5)
    }

    // void "dado un cliente que tiene 10 pesos de saldo y compro un pancho de 15 pesos no puede comprarlo xq no tiene saldo suficiente"() {
    //     given: "dado un cliente que tiene 10 pesos de saldo y un producto pancho de 5 pesos"
    //         def cliente = new Cliente()
    //         cliente.cargarSaldo(new Dinero(10))
    //         def pancho = new Producto(new Dinero(15), 10)

    //     when: "el cliente intenta compra el pancho"
    //         cliente.comprar(pancho)

    //     then: "se lanza una excepcion ya que no le alcanza el saldo"
    //         IllegalStateException exception = thrown()
    // }

    // void "dado un cliente que compra un pancho y habia 10 panchos de stock luego quedan 9 panchos"() {
    //     given: ""
    //         def cliente = new Cliente()
    //         cliente.cargarSaldo(new Dinero(20))
    //         def pancho = new Producto(new Dinero(15), 10)

    //     when: ""
    //         cliente.comprar(pancho)

    //     then: ""
    //         pancho.stock == 9
    // }
}
