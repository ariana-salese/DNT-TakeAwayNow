package buffet

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class PedidoSpec extends Specification implements DomainUnitTest<Pedido> {

    def setup() {
    }

    def cleanup() {
    }

    void "Dado que un cliente agrega varios productos a su pedido, el valor del mismo y la cantidad de productos es la adecuada"() {
        given: "Un cliente y varios productos"
            def cliente = new Cliente()
            def buffet = new Buffet()
            def pancho = new Producto("pancho", 1, new Dinero(90))
            def alfajor = new Producto("alfajor", 1, new Dinero(60))
            def gaseosa = new Producto("gaseosa", 1, new Dinero(100))
            buffet.registrarProducto(pancho)
            buffet.registrarProducto(alfajor)
            buffet.registrarProducto(gaseosa)
            cliente.ingresarBuffet(buffet)

        when: "El cliente los agrega a su pedido"
            cliente.agregarAlPedido("pancho", 1)
            cliente.agregarAlPedido("alfajor", 1)
            cliente.agregarAlPedido("gaseosa", 1)

        then: "La cantidad de productos del pedido y el valor del mismo es el adecuado"
            cliente.cantidadDeProductos() == 3
            cliente.valorDelPedido() == new Dinero(250)
    }

    void "Dado que un cliente intenta agregar un producto del cual acaba de terminarse el stock a su pedido, se lanza una excepción y el valor/cantidad de productos de los pedidos no se ven afectados"() {
        given: "Dos clientes y un único producto"
            def cliente1 = new Cliente()
            def cliente2 = new Cliente()
            def buffet = new Buffet()
            def pancho = new Producto("pancho", 1, new Dinero(90))
            buffet.registrarProducto(pancho)
            cliente1.ingresarBuffet(buffet)
            cliente2.ingresarBuffet(buffet)

        when: "Cuando ambos quieren agregar el mismo producto"
            cliente1.agregarAlPedido("pancho", 1)
            cliente2.agregarAlPedido("pancho", 1)

        then: "Lanza una excepción y la cantidad de productos de los pedidos y sus valores son adecuados"
            cliente1.cantidadDeProductos() == 1
            cliente1.valorDelPedido() == new Dinero(90)

            cliente2.cantidadDeProductos() == 0
            cliente2.valorDelPedido() == new Dinero(0)
            IllegalStateException exception = thrown()
    }

    void "Dado que dos clientes intentan agregar el mismo producto a su pedido y hay stock disponible, el valor/cantidad de productos de los pedidos son adecuados"() {
        given: "Dos clientes y un único producto"
            def cliente1 = new Cliente()
            def cliente2 = new Cliente()
            def buffet = new Buffet()
            def pancho = new Producto("pancho", 2, new Dinero(90))
            buffet.registrarProducto(pancho)
            cliente1.ingresarBuffet(buffet)
            cliente2.ingresarBuffet(buffet)

        when: "Cuando ambos quieren agregar el mismo producto"
            cliente1.agregarAlPedido("pancho", 1)
            cliente2.agregarAlPedido("pancho", 1)

        then: "La cantidad de productos de los pedidos y sus valores son adecuados"
            cliente1.cantidadDeProductos() == 1
            cliente1.valorDelPedido() == new Dinero(90)

            cliente2.cantidadDeProductos() == 1
            cliente2.valorDelPedido() == new Dinero(90)
    }
}
