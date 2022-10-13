package buffet

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class ClienteSpec extends Specification implements DomainUnitTest<Cliente> {

    def setup() {
    }

    def cleanup() {
    }

    void "un cliente puede agregar un producto con stock al carrito"() {
        given: "un cliente que quiere un producto con stock"
            def cliente = new Cliente()
            def buffet = new Buffet()
            def pancho = new Producto(new Dinero(5), 10, "pancho")

        when: "el cliente agrega el producto al carrito"
            buffet.actualizarStock(pancho)
            cliente.ingresarBuffet(buffet)
            cliente.agregarAlPedido("pancho", 1)

        then: "el carrito tiene un producto"
            cliente.pedido.cantidadDeProductos() == 1
    }

    // void "dado un cliente que tiene 10 pesos de saldo y compro un pancho de 5 pesos me terminan quedando de saldo 5 pesos"() {
    //     given: "dado un cliente que tiene 10 pesos de saldo y un producto pancho de 5 pesos"
    //         def cliente = new Cliente()
    //         cliente.cargarSaldo(new Dinero(10))
    //         def pancho = new Producto(new Dinero(5), 10)

    //     when: "el cliente compra el pancho"
    //         cliente.comprar(pancho)

    //     then: "el saldo del cliente es de 5 pesos"
    //         cliente.saldo == new Dinero(5)
    // }

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
