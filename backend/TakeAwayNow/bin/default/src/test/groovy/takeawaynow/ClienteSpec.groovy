package takeawaynow

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import java.time.LocalDateTime

class ClienteSpec extends Specification{

    Negocio negocio
    Cliente cliente
    Horario horario_apertura, horario_cierre
    Date dia

    def setup() {
        horario_apertura = new Horario(9,0)
        horario_cierre = new Horario(18,0)
        negocio = new Negocio("Buffet Paseo Colón", horario_apertura, horario_cierre)
        cliente = new Cliente()
        // year: 2022, 
        // month: 5, 
        // dayOfMonth: 27, 
        // hourOfDay: 12,
        // minute: 0,
        // second: 0
        dia = new Date(2022, 5, 27, 12, 0, 0)
        cliente.ingresarNegocio(negocio, dia)
    }

    def cleanup() {
    }

    void "un cliente no puede ingresar a un negocio que este cerrado"() {
        given: "un horario pasado el horario de cierre"
            // year: 2022, 
            // month: 5, 
            // dayOfMonth: 27, 
            // hourOfDay: 19,
            // minute: 0,
            // second: 0
            Date dia_con_horario_pasado_el_cierre = new Date(2022, 5, 27, 19, 0, 0)

        when: "un cliente ingresa al negocio"
            cliente.ingresarNegocio(negocio, dia_con_horario_pasado_el_cierre)

        then: "se lanza error"
            IllegalStateException exception = thrown()
    }

    void "un cliente puede agregar un producto con stock al pedido"() {
        given: "un cliente que quiere un producto con stock"
            def pancho = new Producto("pancho", 1, new Dinero(10))
            negocio.registrarProducto(pancho)

        when: "el cliente agrega el producto al pedido"
            cliente.agregarAlPedido("pancho", 1)

        then: "el pedido tiene un producto"
            cliente.cantidadDeProductosEnElPedido() == 1
    }

    void "un cliente puede agregar y quitar productos con stock al pedido"() {
        given: "un cliente que quiere un producto con stock"
            def pancho = new Producto("pancho", 5, new Dinero(10))
            negocio.registrarProducto(pancho)

        when: "el cliente agrega dos productos al pedido"
            cliente.agregarAlPedido("pancho", 5)
            cliente.quitarDelPedido("pancho", 3)

        then: "el pedido tiene la cantidad de productos adecuados, y el inventario del negocio vuelve a actualizarse"
            cliente.valorDelPedido() == new Dinero(20)
            cliente.cantidadDeProductosEnElPedido() == 2
            negocio.hayStock("pancho") == true
            negocio.almacen.inventario["pancho"].cantidad == 3
    }

    void "un cliente no puede quitar productos de su pedido los cuales no haya agregado previamente"() {
        when: "un cliente intenta quitar un producto del pedido, pero el mismo está vacío"
            cliente.quitarDelPedido("pancho", 3)

        then:
            Exception e = thrown()
            e.message == "No se pueden quitar 3 panchos del pedido ya que no hay productos en el mismo."
            cliente.valorDelPedido() == new Dinero(0)
            cliente.cantidadDeProductosEnElPedido() == 0
    }

    void "un cliente no puede agregar un producto sin stock sufuciente al pedido"() {
       when: "el cliente intenta agrega el producto sin stock suficiente al pedido"
            def pancho = new Producto("pancho", 1, new Dinero(10))
            negocio.registrarProducto(pancho)
            cliente.agregarAlPedido("pancho", 2)

       then: "el pedido no tiene productos"
            Exception e = thrown()
            e.message == "La cantidad que se desea retirar es mayor al stock actual del producto"
            cliente.pedido.cantidadDeProductos() == 0
    }

    void "un cliente que no agregó productos a su pedido no puede confirmar la compra del mismo"() {
        when: "el cliente confirma la compra de su pedido"
            cliente.confirmarCompraDelPedido()

        then: "se lanza un error"
            Exception e = thrown()
            e.message == "No se puede confirmar la compra del pedido ya que el mismo no tiene productos agregados."
    }

    void "dado un cliente que tiene 10 pesos de saldo y compró un pancho de 5 pesos le terminan quedando de saldo 5 pesos"() {
        given: "dado un cliente que tiene 10 pesos de saldo y un producto pancho de 5 pesos"
            cliente.cargarSaldo(new Dinero(10))
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)

        when: "el cliente compra el pancho"
            cliente.agregarAlPedido("pancho", 1)
            cliente.confirmarCompraDelPedido()

        then: "el saldo del cliente es de 5 pesos"
            cliente.saldo == new Dinero(5)
    }

    void "dado un cliente que tiene 10 pesos de saldo y compró un pancho de 15 pesos, no puede confirmar correctamente la compra de su pedido porque no tiene saldo suficiente"() {
        given: "dado un cliente que tiene 10 pesos de saldo y un producto pancho de 5 pesos"
            cliente.cargarSaldo(new Dinero(10))
            def pancho = new Producto("pancho", 10, new Dinero(15))
            negocio.registrarProducto(pancho)

        when: "el cliente intenta comprar el pancho"
            cliente.agregarAlPedido("pancho", 1)
            cliente.confirmarCompraDelPedido()

        then: "se lanza una excepcion ya que no le alcanza el saldo"
            Exception e = thrown()
            e.message == "No se puede confirmar la compra del pedido ya que el saldo es insuficiente."
    }

    void "dado un cliente que realizó una compra, luego puede verla"() {
        given: "dado un cliente que tiene 10 pesos de saldo y un producto pancho de 5 pesos"
            cliente.cargarSaldo(new Dinero(10))
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)

        when: "el cliente compra el pancho"
            cliente.agregarAlPedido("pancho", 1)
            cliente.confirmarCompraDelPedido()

        then: "el cliente puede ver su compra"
            Map<Integer, Compra> comprasRealizadasDelCliente = cliente.getComprasRealizadas()
            Pedido compraDeUnPancho = comprasRealizadasDelCliente[0].getPedido()

            compraDeUnPancho.cantidadDeProductos() == 1
            compraDeUnPancho.precio() == new Dinero(5)
    }

    void "dado un cliente que realizó varias compras, luego puede verlas todas"() {
        given: "dado un cliente que tiene 10 pesos de saldo, un producto pancho de 5 pesos y un producto coca de 6 pesos"
            cliente.cargarSaldo(new Dinero(11))
            def pancho = new Producto("pancho", 10, new Dinero(5))
            def coca = new Producto("coca", 10, new Dinero(6))
            negocio.registrarProducto(pancho)
            negocio.registrarProducto(coca)

        when: "el cliente compra el pancho y luego la coca"
            cliente.agregarAlPedido("pancho", 1)
            cliente.confirmarCompraDelPedido()
            LocalDateTime fechaDeCompraDelPancho = LocalDateTime.now()

            cliente.agregarAlPedido("coca", 1)
            cliente.confirmarCompraDelPedido()
            LocalDateTime fechaDeCompraDeLaCoca = LocalDateTime.now()

        then: "el cliente puede ver sus compras y los ids son los correctos"
            Map<Integer, Compra> comprasRealizadasDelCliente = cliente.getComprasRealizadas()
            
            Pedido compraDeUnPancho = comprasRealizadasDelCliente[0].getPedido()
            compraDeUnPancho.cantidadDeProductos() == 1
            compraDeUnPancho.precio() == new Dinero(5)
            comprasRealizadasDelCliente[0].getFecha().getHour() == fechaDeCompraDelPancho.getHour()
            comprasRealizadasDelCliente[0].getFecha().getMinute() == fechaDeCompraDelPancho.getMinute()
            comprasRealizadasDelCliente[0].getId() == 0
            
            Pedido compraDeUnaCoca = comprasRealizadasDelCliente[1].getPedido()
            compraDeUnaCoca.cantidadDeProductos() == 1
            compraDeUnaCoca.precio() == new Dinero(6)
            comprasRealizadasDelCliente[1].getFecha().getHour() == fechaDeCompraDeLaCoca.getHour()
            comprasRealizadasDelCliente[1].getFecha().getMinute() == fechaDeCompraDeLaCoca.getMinute()
            comprasRealizadasDelCliente[1].getId() == 1
    }

    void "dado un cliente que no realizó compras, el mismo no puede retirar ninguna compra"() {
        when: "el cliente quiere retirar una compra"
            cliente.retirarCompra(0)

        then: "se lanza el error correspondiente"
            Exception e = thrown()
            e.message == "No se encuentra una compra realizada con el ID indicado."
    }

    void "dado un cliente que realizó una compra y la misma aún no está lista para retirar, cuando el cliente intenta retirarla obtiene un error"() {
        given: "un cliente que confirma la compra de su pedido"
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)
            
            cliente.cargarSaldo(new Dinero(10))
            cliente.agregarAlPedido("pancho", 2)
            cliente.confirmarCompraDelPedido()

            negocio.prepararCompra(0)
            int puntosObtenidosPreviamente = cliente.getPuntosDeConfianza()

        when: "intenta retirar dicha compra"
            cliente.retirarCompra(0)

        then: "obtiene el error correspondiente y sus puntos de confianza siguen igual que antes"
            Exception e = thrown()
            e.message == "La compra aún no está lista para retirar, su estado actual es EN_PREPARACION."

            cliente.getPuntosDeConfianza() == puntosObtenidosPreviamente
    }

    void "dado un cliente que realizó una compra y la misma está lista para retirar, tras retirarla sus puntos de confianza se actualizan y el id de la compra corresponde a una compra retirada"() {
        given: "un cliente que confirma la compra de su pedido"
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)
            
            cliente.cargarSaldo(new Dinero(5))
            cliente.agregarAlPedido("pancho", 1)
            cliente.confirmarCompraDelPedido()

        when: "la misma está lista para retirar y el mismo la quiere retirar"
            negocio.prepararCompra(0)
            negocio.marcarCompraListaParaRetirar(0)
            cliente.retirarCompra(0)

        then: "la compra se retiró con éxito, los puntos de confianza se actualizan y el id de la compra corresponde a una compra retirada"
            cliente.getPuntosDeConfianza() == 1
            cliente.getComprasRetiradas().contains(0) == true
    }

    void "dado un cliente que realizó una compra y la retiró, obtiene un error tras intentar retirarla nuevamente"() {
        given: "un cliente que confirma la compra de su pedido y luego lo retira"
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)
            
            cliente.cargarSaldo(new Dinero(10))
            cliente.agregarAlPedido("pancho", 2)
            cliente.confirmarCompraDelPedido()

            negocio.prepararCompra(0)
            negocio.marcarCompraListaParaRetirar(0)
            cliente.retirarCompra(0)
            int puntosObtenidosPreviamente = cliente.getPuntosDeConfianza()

        when: "intenta retirar dicha compra nuevamente"
            cliente.retirarCompra(0)

        then: "obtiene el error correspondiente y sus puntos de confianza siguen igual que antes (luego de retirar su pedido la primera vez)"
            Exception e = thrown()
            e.message == "Dicha compra ya fue retirada previamente."

            cliente.getPuntosDeConfianza() == puntosObtenidosPreviamente
    }

    void "dado un cliente que realizó una compra y la cancela cuando la misma se encuentra aguardando preparación entonces pierde un punto de confianza, obtiene nuevamente su dinero y el stock de dichos productos se actualiza"() {
        given: "un cliente que confirma la compra de su pedido (compra 2 panchos, el stock de los panchos post compra es 8)"
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)
            
            cliente.cargarSaldo(new Dinero(10))
            cliente.agregarAlPedido("pancho", 2)
            
            Dinero saldoPreCompra = cliente.getSaldo()
            int puntosObtenidosPreviamente = cliente.getPuntosDeConfianza()

            cliente.confirmarCompraDelPedido()

        when: "lo cancela inmediatamente (tiene estado de aguardando preparación)"
            cliente.cancelarCompra(0)

        then: "pierde un punto de confianza, obtiene nuevamente su dinero y el stock de dichos productos se actualiza (el stock de los panchos post cancelación es 10)"
            cliente.getPuntosDeConfianza() == puntosObtenidosPreviamente - 1
            cliente.getSaldo().getMonto() == saldoPreCompra.getMonto()
            negocio.getAlmacen().getInventario()["pancho"].getCantidad() == 10
    }

    void "dado un cliente que realizó una compra y la cancela cuando la misma se encuentra en preparación entonces pierde varios punto de confianza, no obtiene nuevamente su dinero y el stock de dichos productos se actualiza"() {
        given: "un cliente que confirma la compra de su pedido y el negocio lo comienza a preparar"
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)
            
            cliente.cargarSaldo(new Dinero(20))
            cliente.agregarAlPedido("pancho", 4)
            
            Dinero saldoPreCompra = cliente.getSaldo()
            int puntosObtenidosPreviamente = cliente.getPuntosDeConfianza()

            cliente.confirmarCompraDelPedido()
            negocio.prepararCompra(0)

        when: "cancela la compra cuando el pedido esta en preparación"
            cliente.cancelarCompra(0)

        then: "pierde puntos de confianza (cantidad total de productos en la compra), no obtiene nuevamente su dinero y el stock de dichos productos se actualiza (el stock de los panchos post cancelación es 10)"
            cliente.getPuntosDeConfianza() == puntosObtenidosPreviamente - 4
            cliente.getSaldo().getMonto() == saldoPreCompra.getMonto() - cliente.getComprasRealizadas()[0].getPedido().precio().getMonto()
            negocio.getAlmacen().getInventario()["pancho"].getCantidad() == 10
    }

    void "dado un cliente que realizó una compra y la cancela cuando la misma se encuentra lista para retirar entonces pierde todos sus puntos de confianza, no obtiene nuevamente su dinero y el stock de dichos productos se actualiza"() {
        given: "un cliente que confirma la compra de su pedido, el negocio lo comienza a preparar y luego la compra está lista para retirar"
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)
            
            cliente.cargarSaldo(new Dinero(20))
            cliente.agregarAlPedido("pancho", 4)
            
            Dinero saldoPreCompra = cliente.getSaldo()
            int puntosObtenidosPreviamente = cliente.getPuntosDeConfianza()

            cliente.confirmarCompraDelPedido()
            negocio.prepararCompra(0)
            negocio.marcarCompraListaParaRetirar(0)

        when: "cancela la compra cuando el pedido esta listo para retirar"
            cliente.cancelarCompra(0)

        then: "pierde todos sus puntos de confianza, no obtiene nuevamente su dinero y el stock de dichos productos se actualiza (el stock de los panchos post cancelación es 10)"
            cliente.getPuntosDeConfianza() == 0
            cliente.getSaldo().getMonto() == saldoPreCompra.getMonto() - cliente.getComprasRealizadas()[0].getPedido().precio().getMonto()
            negocio.getAlmacen().getInventario()["pancho"].getCantidad() == 10
    }

    void "dado un cliente que realizó una compra pero hubo algún error en la entrega del pedido por parte del negocio y pasaron menos de 5 minutos desde que se retiró, el cliente solicita la devolución de la compra y obtiene nuevamente su dinero y el stock de dichos productos se actualiza"() {
        given: "un cliente que confirma la compra de su pedido, el negocio lo comienza a preparar y luego la compra está lista para retirar"
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)
            
            cliente.cargarSaldo(new Dinero(20))
            cliente.agregarAlPedido("pancho", 4)
            
            Dinero saldoPreCompra = cliente.getSaldo()
            int puntosObtenidosPreviamente = cliente.getPuntosDeConfianza()

            cliente.confirmarCompraDelPedido()
            negocio.prepararCompra(0)
            negocio.marcarCompraListaParaRetirar(0)
            cliente.retirarCompra(0)

        when: "el cliente solicita la devolución de la compra"
            cliente.solicitarDevoluciónDelPedido(0)

        then: "obtiene nuevamente su dinero y el stock de dichos productos se actualiza (el stock de los panchos post cancelación es 10) y sus puntos de confianza no se modifican"
            cliente.getPuntosDeConfianza() == puntosObtenidosPreviamente
            cliente.getSaldo() == new Dinero(20)
            negocio.getAlmacen().getInventario()["pancho"].getCantidad() == 10
    }
}
