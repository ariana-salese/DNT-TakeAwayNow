package buffet

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class PedidoSpec extends Specification implements DomainUnitTest<Pedido> {

    Buffet buffet
    Cliente lautaro, ariana
    Producto alfajor, gaseosa, pancho

    def setup() {
        buffet = new Buffet()
        
        lautaro = new Cliente()
        ariana = new Cliente()
        
        alfajor = new Producto("alfajor", 1, new Dinero(60))
        gaseosa = new Producto("gaseosa", 1, new Dinero(100))
        pancho = new Producto("pancho", 1, new Dinero(90))
        
    }

    def cleanup() {
    }

    void "Dado que un cliente agrega varios productos registrados a su pedido, el valor del mismo y la cantidad de productos es la adecuada"() {
        given: "Un cliente y varios productos"
            buffet.registrarProducto(pancho)
            buffet.registrarProducto(alfajor)
            buffet.registrarProducto(gaseosa)
            lautaro.ingresarBuffet(buffet)

        when: "El cliente los agrega a su pedido"
            lautaro.agregarAlPedido("pancho", 1)
            lautaro.agregarAlPedido("alfajor", 1)
            lautaro.agregarAlPedido("gaseosa", 1)

        then: "La cantidad de productos del pedido y el valor del mismo es el adecuado"
            lautaro.cantidadDeProductosEnElPedido() == 3
            lautaro.valorDelPedido() == new Dinero(250)
    }

    void "Dado que un cliente intenta agregar un producto del cual acaba de terminarse el stock a su pedido, se lanza una excepción y el valor/cantidad de productos de los pedidos no se ven afectados"() {
        given: "Dos clientes y un único producto"
            buffet.registrarProducto(pancho)
            lautaro.ingresarBuffet(buffet)
            ariana.ingresarBuffet(buffet)

        when: "Ambos quieren agregar el mismo producto"
            lautaro.agregarAlPedido("pancho", 1)
            ariana.agregarAlPedido("pancho", 1)

        then: "Lanza una excepción y la cantidad de productos de los pedidos y sus valores son adecuados"
            lautaro.cantidadDeProductosEnElPedido() == 1
            lautaro.valorDelPedido() == new Dinero(90)

            ariana.cantidadDeProductosEnElPedido() == 0
            ariana.valorDelPedido() == new Dinero(0)
            Exception e = thrown()
            e.message == "La cantidad que se desea retirar es mayor al stock actual del producto"
    }

    void "Dado que dos clientes intentan agregar el mismo producto a su pedido y hay stock disponible, el valor/cantidad de productos de los pedidos son adecuados"() {
        given: "Dos clientes y un único producto"
            buffet.registrarProducto(pancho)
            buffet.ingresarStock("pancho", 1)
            lautaro.ingresarBuffet(buffet)
            ariana.ingresarBuffet(buffet)

        when: "Ambos quieren agregar el mismo producto"
            lautaro.agregarAlPedido("pancho", 1)
            ariana.agregarAlPedido("pancho", 1)

        then: "La cantidad de productos de los pedidos y sus valores son adecuados"
            lautaro.cantidadDeProductosEnElPedido() == 1
            lautaro.valorDelPedido() == new Dinero(90)

            ariana.cantidadDeProductosEnElPedido() == 1
            ariana.valorDelPedido() == new Dinero(90)
    }
}
