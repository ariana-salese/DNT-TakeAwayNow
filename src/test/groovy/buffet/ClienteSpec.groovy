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

    void "dado un cliente que tiene 10 pesos de saldo y compro un pancho de 15 pesos no puede comprarlo porque no tiene saldo suficiente"() {
        given: "dado un cliente que tiene 10 pesos de saldo y un producto pancho de 5 pesos"
            def cliente = new Cliente()
            cliente.cargarSaldo(new Dinero(10))
            def buffet = new Buffet()
            def pancho = new Producto("pancho", 10, new Dinero(15))

        when: "el cliente intenta compra el pancho"
            buffet.registrarProducto(pancho)
            cliente.ingresarBuffet(buffet)
            cliente.agregarAlPedido("pancho", 1)
            cliente.comprar()

        then: "se lanza una excepcion ya que no le alcanza el saldo"
            IllegalStateException exception = thrown()
    }

    void "dado un cliente que realizó una compra, luego puede verla"() {
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

        then: "el cliente puede ver su compra"
            List<Compra> historial = cliente.historialDeCompras()
            Pedido compraPancho = historial.first().pedido()

            compraPancho.cantidadDeProductos() == 1
            compraPancho.precio() == new Dinero(5)
    }


    void "dado un cliente que realizó varias compras, luego puede ver las que compras"() {
        given: "dado un cliente que tiene 10 pesos de saldo, un producto pancho de 5 pesos y un producto coca de 6 pesos"
            def cliente = new Cliente()
            cliente.cargarSaldo(new Dinero(11))
            def buffet = new Buffet()
            def pancho = new Producto("pancho", 10, new Dinero(5))
            def coca = new Producto("coca", 10, new Dinero(6))

        when: "el cliente compra el pancho y luego la coca"
            buffet.registrarProducto(pancho)
            buffet.registrarProducto(coca)
            cliente.ingresarBuffet(buffet)
            cliente.agregarAlPedido("pancho", 1)
            cliente.comprar()
            cliente.agregarAlPedido("coca", 1)
            cliente.comprar()

        then: "el cliente puede ver sus compras"
            List<Compra> historial = cliente.historialDeCompras()
            Pedido compraPancho = historial.first().pedido()
            Pedido compraCoca = historial.last().pedido()

            compraPancho.cantidadDeProductos() == 1
            compraPancho.precio() == new Dinero(5)
            compraCoca.cantidadDeProductos() == 1
            compraCoca.precio() == new Dinero(6)
    }
}
