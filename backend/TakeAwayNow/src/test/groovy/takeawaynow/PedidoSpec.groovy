package takeawaynow

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class PedidoSpec extends Specification implements DomainUnitTest<Pedido> {

    Negocio negocio
    Cliente lautaro, ariana
    Producto alfajor, gaseosa, pancho
    Horario horario_apertura, horario_cierre
    Date dia

    def setup() {
        horario_apertura = new Horario(9,0)
        horario_cierre = new Horario(18,0)
        negocio = new Negocio("buffet Paseo Colón", horario_apertura, horario_cierre)
        
        lautaro = new Cliente()
        ariana = new Cliente()
        
        alfajor = new Producto("alfajor", 1, new Dinero(60))
        gaseosa = new Producto("gaseosa", 1, new Dinero(100))
        pancho = new Producto("pancho", 1, new Dinero(90))
        // year: 2022, 
        // month: 5, 
        // dayOfMonth: 27, 
        // hourOfDay: 12,
        // minute: 0,
        // second: 0
        dia = new Date(2022, 5, 27, 12, 0, 0)
    }

    def cleanup() {
    }

    void "Dado que un cliente agrega varios productos registrados a su pedido, el valor del mismo y la cantidad de productos es la adecuada"() {
        given: "Un cliente y varios productos"
            negocio.registrarProducto(pancho)
            negocio.registrarProducto(alfajor)
            negocio.registrarProducto(gaseosa)
            lautaro.ingresarNegocio(negocio, dia)

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
            negocio.registrarProducto(pancho)
            lautaro.ingresarNegocio(negocio, dia)
            ariana.ingresarNegocio(negocio, dia)

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
            negocio.registrarProducto(pancho)
            negocio.ingresarStock("pancho", 1)
            lautaro.ingresarNegocio(negocio, dia)
            ariana.ingresarNegocio(negocio, dia)

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
