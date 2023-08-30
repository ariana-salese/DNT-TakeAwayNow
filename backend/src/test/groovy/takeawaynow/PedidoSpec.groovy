package takeawaynow

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import java.time.LocalDateTime
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month

class PedidoSpec extends Specification implements DomainUnitTest<Pedido> {

    Negocio negocio
    Cliente lautaro, ariana
    Producto alfajor, gaseosa, pancho, chicle
    LocalTime horario_apertura, horario_cierre
    LocalDateTime dia

    def setup() {
        horario_apertura = LocalTime.of(9,0)
        horario_cierre = LocalTime.of(18,0)
        negocio = new Negocio("buffet Paseo Colón", horario_apertura, horario_cierre)
        LocalDate diaDeCumpleanios = LocalDate.of(2001, Month.MAY, 27)
        lautaro = new Cliente("Messi", "campeondelmundo", diaDeCumpleanios)
        ariana = new Cliente("dibu", "if***youtwice", diaDeCumpleanios)
        
        alfajor = new Producto("alfajor", 1, new Dinero(60), new PuntosDeConfianza(70))
        gaseosa = new Producto("gaseosa", 1, new Dinero(100), new PuntosDeConfianza(60))
        pancho = new Producto("pancho", 1, new Dinero(90),  new PuntosDeConfianza(80))
        chicle = new Producto("chicle", 1, new Dinero(30))
        // year: 2022, 
        // month: 5, 
        // dayOfMonth: 27, 
        // hourOfDay: 12,
        // minute: 0,
        // second: 0
        dia = LocalDateTime.of(2022, Month.MAY, 27, 12, 0, 0)
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
            lautaro.valorDelPedidoEnDinero() == new Dinero(250)
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
            lautaro.valorDelPedidoEnDinero() == new Dinero(90)

            ariana.cantidadDeProductosEnElPedido() == 0
            ariana.valorDelPedidoEnDinero() == new Dinero(0)
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
            lautaro.valorDelPedidoEnDinero() == new Dinero(90)

            ariana.cantidadDeProductosEnElPedido() == 1
            ariana.valorDelPedidoEnDinero() == new Dinero(90)
    }

    //TODO los test de arriba creo que testean cosas de mas, estamos testeando cliente y negocio al pp,
    //me pa que solo deberiamos usar un pedido porque estamos testeando pedido y no necesitamos a los otro

    void "Un pedido con productos agregado por puntos de confianza tiene valor correcto"() {
        given: "Un pedido"
            Pedido pedido = new Pedido()

        when: "Se agregan productos a cambio de puntos"
            pedido.agregarPorPuntosDeConfianza(pancho)
            pedido.agregarPorPuntosDeConfianza(alfajor)
            pedido.agregarPorPuntosDeConfianza(gaseosa)

        then: "La cantidad de productos del pedido y el valor del mismo es el adecuado"
            pedido.cantidadDeProductos() == 3
            pedido.precio() == new Dinero(0)
            pedido.puntos() == new PuntosDeConfianza(210)
    }

    void "Un pedido con dos productos agregados por puntos de confianza y uno por dinero tiene valor correcto"() {
        given: "Un pedido"
            Pedido pedido = new Pedido()

        when: "Se agregan dos productos a cambio de puntos y uno a cambio de dinero"
            pedido.agregarPorPuntosDeConfianza(pancho)
            pedido.agregarPorPuntosDeConfianza(alfajor)
            pedido.agregar(gaseosa)

        then: "La cantidad de productos del pedido y el valor del mismo es el adecuado"
            pedido.cantidadDeProductos() == 3
            pedido.precio() == new Dinero(100)
            pedido.puntos() == new PuntosDeConfianza(150)
    }

    void "Agregar un producto no canjeable por puntos de confianza, por puntos de confianza, lanza error"() {
        given: "Un pedido"
            Pedido pedido = new Pedido()

        when: "Se agregan un producto no canjeable por puntos a cambio de puntos"
            pedido.agregarPorPuntosDeConfianza(chicle)

        then: "Se lanza error"
            pedido.cantidadDeProductos() == 0
            IllegalStateException exception = thrown()
    }
}
